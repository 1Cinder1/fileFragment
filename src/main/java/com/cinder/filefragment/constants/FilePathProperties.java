package com.cinder.filefragment.constants;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author cinder
 */
@Data
//@ConfigurationProperties("file.fragment")
public class FilePathProperties {
  private String inputPath;

  private String outputPath;
}
