package com.example.ecommerce.services;



import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.ecommerce.utils.DocHelper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.ecommerce.common.configurations.aws.AmazonClientService;
import com.example.ecommerce.common.enums.DocType;
import com.example.ecommerce.common.exception.ResourceNotFoundException;
import com.example.ecommerce.dtos.requests.UpdateFileRequest;
import com.example.ecommerce.dtos.responses.DocumentMappingResponse;
import com.example.ecommerce.entities.DocumentMapping;
import com.example.ecommerce.repositories.DocumentMappingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final AmazonClientService amazonClientService;
    private final DocumentMappingRepository docManageRepository;
    public Map<String,Object> uploadFile(List<MultipartFile> files, String uuid) {
        Map<String, Object> res = new HashMap<>();
        List<Integer> failed = new ArrayList<>();
        List<DocumentMapping> passed = new ArrayList<>();
        int maxPosition=getMaxPosition(uuid)+1;
        for(int i=0;i<files.size();i++){
            DocType docType = DocHelper.isImageContentType(files.get(i).getContentType().toLowerCase()) ? DocType.IMAGE
                    : DocType.DOC;
            String path = DocHelper.getS3Path(docType, files.get(i).getOriginalFilename(), uuid);
            if(amazonClientService.uploadFile(files.get(i), path)){
                passed.add(DocumentMapping
                            .builder()
                            .type(docType)
                            .fileType(files.get(i).getContentType())
                            .url(path)
                            .uuid(uuid)
                            .position(maxPosition+i)
                            .build());
                }else{
                        failed.add(i);
                    }
        }
        res.put("uploaded", ((List<DocumentMapping>) docManageRepository.saveAll(passed)).stream()
                .map(DocumentMappingResponse::fromModel).sorted(Comparator.comparingInt(
                        DocumentMappingResponse::getPosition))
                .toList());
        res.put("failed", failed);
        return res;
    }
    public Boolean uploadByte(byte[] data, String uuid,DocType assetType,String fileName,String fileType,int position){
        String path = DocHelper.getS3Path(assetType, fileName, uuid);
        if (amazonClientService.uploadByte(data, path, fileType)) {
            DocumentMapping documentMapping=DocumentMapping
                    .builder()
                    .type(DocHelper.isImageContentType(fileType.toLowerCase()) ? DocType.IMAGE
                            : DocType.DOC)
                    .fileType(fileType)
                    .url(path)
                    .uuid(uuid)
                    .position(position)
                    .build();
                docManageRepository.save(documentMapping);
                return true;
        } 
        return false;
    }
    public Map<String,Object> updatePosition(UpdateFileRequest fileRequests) {
        checkUuid(fileRequests.getUuid());
        Map<String, Object> res = new HashMap<>();
        List<Long> failed = new ArrayList<>();
        List<DocumentMapping> passed = new ArrayList<>();
        Set<Long> existsingIds = docManageRepository.findByUuidAndTypeOrderByPosition(fileRequests.getUuid(),
                        fileRequests.isDoc() ? DocType.DOC : DocType.IMAGE
                        ).stream().map(DocumentMapping::getId).collect(Collectors.toSet());
        Set<Long> difference = new HashSet<>(existsingIds);
        difference.removeAll(fileRequests.getIds());
        fileRequests.getIds().addAll(difference);
        int pos=1;
        for (Long id:fileRequests.getIds()) {
            if(existsingIds.contains(id)){
                DocumentMapping doc = getById(id);
                 doc.setPosition(pos);
                 pos++;
                passed.add(doc);
                }else{
                    failed.add(id);
                }   
        }
        res.put("FailedToUpdateIds",failed);
        res.put("UpdatedFileIds", ((List<DocumentMapping>) docManageRepository.saveAll(passed)).stream()
                .map(DocumentMappingResponse::fromModel).sorted(Comparator.comparingInt(
                        DocumentMappingResponse::getPosition))
                .toList());
        return res;
    }
    private void checkUuid(String uuid) {
        if (!docManageRepository.existsByUuid(uuid))
            throw new ResourceNotFoundException("No files/images exists by this uuid");
    }

    private int getMaxPosition(String uuid){
        if(docManageRepository.existsByUuid(uuid))
            return docManageRepository.findMaxPositionByUuid(uuid);
        return 0;
    }
    public List<DocumentMappingResponse> getFilesByUuid(String uuid){
        return docManageRepository.findAllByUuid(uuid).stream().map(DocumentMappingResponse::fromModel).sorted(Comparator.comparingInt(
                DocumentMappingResponse::getPosition))
                .toList();

    }
    public void deleteFileById(Long id) {
        DocumentMapping documentMapping =docManageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File not found by id " + id));
        docManageRepository.delete(documentMapping);
    }
    private DocumentMapping getById(Long id) {
        return docManageRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("File not found by id "+id));
    }
    public boolean uploadXMLToAWS(String fileName,byte[] data){
        return amazonClientService.uploadByte(data, fileName, "XML");
    }

    public String fetchXmlString(String feedName){
        return amazonClientService.getXmlFileAsString(feedName);
    }
}
