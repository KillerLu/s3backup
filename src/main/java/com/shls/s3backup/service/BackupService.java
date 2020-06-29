package com.shls.s3backup.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shls.s3backup.dao.BackupRecordDao;
import com.shls.s3backup.dao.BackupTaskDao;
import com.shls.s3backup.util.*;
import com.shls.s3backup.view.BackupRecord;
import com.shls.s3backup.view.BackupTask;
import com.shls.s3backup.vo.BackupRecordVo;
import com.shls.s3backup.vo.BackupTaskVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ：Killer
 * @date ：Created in 20-5-13 上午9:28
 * @description：${description}
 * @modified By：
 * @version: version
 */
@Service
public class BackupService {

    private static final Logger logger = LoggerFactory.getLogger(BackupService.class);

    @Autowired
    private AmazonS3 srcAmazonS3;
    @Autowired
    private AmazonS3 desAmazonS3;
    @Value("${backup.prefix}")
    private String prefix;
    @Value("${transfer.put.maxsize}")
    private int putMaxSize;

    /**
     * 全量备份
     */
    public static final int BACKUP_ALL = 1;
    /**
     * 备份上一天(增量备份)
     */
    public static final int BACKUP_BY_DATE = 2;

    /**
     * 定时备份昨天
     */
    public static final int REGULAR_BACKUP=3;

    /**
     * 备份结果
     */
    public static final int BACKUP_SUCCESS=1;

    public static final int BACKUP_FAIL=2;

    /**
     * 任务状态
     */
    public static final int TASK_FINISH=1;

    public static final int TASK_RUNNING=0;

    public static final int TASK_SUSPEND=2;

    @Autowired
    private BackupRecordDao backupRecordDao;
    @Autowired
    private BackupTaskDao backupTaskDao;

    public void backupBacket(String bucketName,Long taskId,Integer backupType) {
        List<S3ObjectSummary> summarys = listBucketObject(bucketName,backupType);
        if (!CollectionUtils.isEmpty(summarys)) {
            logger.info("开始备份" + bucketName + "bucket,共" + summarys.size() + "个文件");
            for (S3ObjectSummary summary : summarys) {
                String key = summary.getKey();
                int count = backupRecordDao.selectCount(new QueryWrapper<BackupRecord>().eq("bucket", bucketName).eq("unique_key", key).ne("backup_result", BACKUP_FAIL));
                if (count > 0) {
                    continue;
                }
                BackupRecord record = new BackupRecord();
                record.setTaskId(taskId);
                try {
                    S3Object s3Object = srcAmazonS3.getObject(bucketName, key);
                    record.setSourceDate(DateUtil.format(s3Object.getObjectMetadata().getLastModified(), DateUtil.YYYY_MM_DD));
                    record.setSize(s3Object.getObjectMetadata().getContentLength());
                    if (s3Object != null) {
                        if (s3Object.getObjectMetadata().getContentLength() > putMaxSize) {
                            logger.info("开始备份大文件,bucket:" + bucketName + "key:" + key + "文件大小:" + s3Object.getObjectMetadata().getContentLength());
                            partPutObjecct(desAmazonS3, StringUtils.isEmpty(prefix) ? bucketName : bucketName + prefix, key, s3Object);
                        } else {
                            putObject(desAmazonS3, StringUtils.isEmpty(prefix) ? bucketName : bucketName + prefix, key, s3Object);
                        }
                    }
                    record.setBackupResult(BACKUP_SUCCESS);
                } catch (Exception e) {
                    record.setBackupResult(BACKUP_FAIL);
                    if (e.getMessage().contains("AccessDenied")) {
                        logger.error(bucketName+"无权限写入");
                        break;
                    }
                    logger.error("bucket:" + bucketName + ",key:" + key + "备份失败", e);
                    continue;
                }finally {
                    record.setBucket(bucketName);
                    record.setUniqueKey(key);
                    record.setBackupType(backupType);
                    record.setBackupDate(DateUtil.format(DateUtil.getNow(), DateUtil.YYYY_MM_DD));
                    backupRecordDao.insert(record);
                }
            }

        }
    }

    private List<S3ObjectSummary> listBucketObject(String bucketName,Integer backupType) {
        ObjectListing listing = srcAmazonS3.listObjects(bucketName);
        List<S3ObjectSummary> summaries = listing.getObjectSummaries();

        while (listing.isTruncated()) {
            listing = srcAmazonS3.listNextBatchOfObjects (listing);
            summaries.addAll (listing.getObjectSummaries());
        }
        List<S3ObjectSummary> returnSummaries = new ArrayList<S3ObjectSummary>();
        if (!CollectionUtils.isEmpty(summaries)) {
            for (S3ObjectSummary summary : summaries) {
                if (checkShouldBackup(summary,backupType)) {
                    returnSummaries.add(summary);
                }
            }
        }
        return returnSummaries;
    }

    private boolean checkShouldBackup(S3ObjectSummary summary,Integer backupType) {
        switch (backupType) {
            case BACKUP_ALL:
                return true;
            case REGULAR_BACKUP:
                return summary.getLastModified().getTime()>= DateUtil.getDayStartTime(DateUtil.getNow(), -1).getTime()&&
                        summary.getLastModified().getTime()<=DateUtil.getDayEndTime(DateUtil.getNow(), -1).getTime();
            case BACKUP_BY_DATE:
                return summary.getLastModified().getTime()>= DateUtil.getDayStartTime(DateUtil.getNow(), 0).getTime()&&
                        summary.getLastModified().getTime()<=DateUtil.getDayEndTime(DateUtil.getNow(), 0).getTime();
            default:
                return false;
        }
    }

    public PutObjectResult putObject(AmazonS3 amazonS3, String bucket, String key, S3Object s3Object) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        InputStream inputStream = s3Object.getObjectContent();
        objectMetadata.setContentLength(s3Object.getObjectMetadata().getContentLength());
        objectMetadata.setContentType(s3Object.getObjectMetadata().getContentType());
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, inputStream, objectMetadata);
        putObjectRequest.getRequestClientOptions().setReadLimit(2147483647);
        if (!amazonS3.doesBucketExistV2(bucket)) {
            amazonS3.createBucket(bucket);
        }
        logger.info(key + " start upload ");
        PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);
        logger.info(key + " is upload ok");
        return putObjectResult;
    }


    public void partPutObjecct(AmazonS3 amazonS3, String bucket, String key, S3Object s3Object) throws Exception {
        if (!amazonS3.doesBucketExistV2(bucket)) {
            amazonS3.createBucket(bucket);
        }
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        // 创建一个列表保存所有分传的 PartETag, 在分段完成后会用到
        List<PartETag> partETags = new ArrayList<>();
        // 第一步，初始化，声明下面将有一个 Multipart Upload
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucket, key);
        InitiateMultipartUploadResult initResponse = amazonS3.initiateMultipartUpload(initRequest);
        ByteArrayOutputStream bos = null;
        try {
            int size = 1024;
            byte[] buffer = new byte[size];
            int len;
            int partNum = 1;
            bos = new ByteArrayOutputStream();
            int totalLength=0;
            while ((len = inputStream.read(buffer, 0, size)) != -1) {
                totalLength+=len;
                bos.write(buffer, 0, len);
                if (bos.size() >= putMaxSize) {
                    UploadPartRequest uploadRequest = new UploadPartRequest()
                            .withBucketName(bucket).withKey(key)
                            .withUploadId(initResponse.getUploadId()).withPartNumber(partNum++)
                            .withInputStream(new ByteArrayInputStream(bos.toByteArray()))
                            .withPartSize(totalLength);
                    partETags.add(
                            amazonS3.uploadPart(uploadRequest).getPartETag());
                    bos.close();
                    bos = new ByteArrayOutputStream();
                    totalLength=0;
                }
            }
            UploadPartRequest uploadRequest = new UploadPartRequest()
                    .withBucketName(bucket).withKey(key)
                    .withUploadId(initResponse.getUploadId()).withPartNumber(partNum++)
                    .withInputStream(new ByteArrayInputStream(bos.toByteArray()))
                    .withPartSize(totalLength)
                    .withLastPart(true);
            // 第二步，上传分段，并把当前段的 PartETag 放到列表中
            partETags.add(amazonS3.uploadPart(uploadRequest).getPartETag());
            // 第三步，完成上传
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucket, key, initResponse.getUploadId(), partETags);
            amazonS3.completeMultipartUpload(compRequest);
        } catch (Exception e) {
            amazonS3.abortMultipartUpload(new AbortMultipartUploadRequest(bucket, key, initResponse.getUploadId()));
            //将异常往上抛
            throw e;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }
        }
    }

    public void backupS3(Integer backupType) {
        BackupTask task=new BackupTask();
        task.setBackupType(backupType);
        task.setStartTime(DateUtil.getNow());

        backupTaskDao.insert(task);
        ThreadPoolUtils.execute(() -> {
            List<Bucket> bucketList = srcAmazonS3.listBuckets();
            if (!CollectionUtils.isEmpty(bucketList)) {
                for (Bucket bucket : bucketList) {
                    backupBacket(bucket.getName(),task.getId(),backupType);
                }
            }
            task.setEndTime(DateUtil.getNow());
            task.setTaskStatus(TASK_FINISH);
            backupTaskDao.updateById(task);
        });

    }


    public ListAndCount<BackupTaskVo> listBackupTaskDetail(String backupCeph,Integer page, Integer pageLength) {
        ListAndCount<BackupTaskVo> listAndCount = new ListAndCount<BackupTaskVo>();
        List<BackupTaskVo> vos= backupTaskDao.listBackupTaskDetail(backupCeph,page<=0?0:pageLength*(page-1), pageLength);
        if (!CollectionUtils.isEmpty(vos)) {
            for (BackupTaskVo vo : vos) {
                vo.setReadableSize(FileUtil.readableFileSize(vo.getSize()));
            }
        }
        listAndCount.setList(vos);
        listAndCount.setCount(backupTaskDao.selectCount(null));
        return listAndCount;
    }


    public ListAndCount<BackupRecordVo> listBackupRecordByTaskId(Long taskId, Integer page, Integer pageLength) {
        IPage<BackupRecord> pages = backupRecordDao.selectPage(new Page<BackupRecord>(page, pageLength), new QueryWrapper<BackupRecord>().eq("task_id", taskId));
        ListAndCount<BackupRecordVo> listAndCount = new ListAndCount<BackupRecordVo>();
        List<BackupRecordVo> vos = CloneUtil.batchClone(pages.getRecords(), BackupRecordVo.class);
        for (BackupRecordVo vo : vos) {
            vo.setReadableSize(FileUtil.readableFileSize(vo.getSize()==null?0:vo.getSize()));
        }
        listAndCount.setList(vos);
        listAndCount.setCount(pages.getTotal());
        return listAndCount;
    }

    public List<BackupTask> listRunningTasks() {
        return backupTaskDao.selectList(new QueryWrapper<BackupTask>().eq("backup_type", REGULAR_BACKUP).eq("task_status", TASK_RUNNING));
    }

    public void suspendRunningTask() {
        List<BackupTask> tasks=listRunningTasks();
        if (!CollectionUtils.isEmpty(tasks)) {
            for (BackupTask task : tasks) {
                task.setTaskStatus(TASK_SUSPEND);
                backupTaskDao.updateById(task);
            }
        }
    }
}
