package com.neeraj.projects.lovableclone.service;

import com.neeraj.projects.lovableclone.dto.project.FileContentResponse;
import com.neeraj.projects.lovableclone.dto.project.FileNode;

import java.util.List;

public interface FileService {
    List<FileNode> getFileTree(Long projectId, Long userId);

    FileContentResponse getFileContent(Long projectId, String path, Long userId);
}
