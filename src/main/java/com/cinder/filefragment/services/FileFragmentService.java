package com.cinder.filefragment.services;

/**
 * @author cinder
 */
public interface FileFragmentService {
  void encryptFileForLocal(String inputPath,String outputPath,String password);

  void encryptFileForRemote();

  void uploadFile();
}
