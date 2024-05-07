package com.cinder.filefragment.threadPool;

import com.cinder.filefragment.timer.TimerCollector;
import com.cinder.filefragment.vo.FragmentFile;
import com.cinder.filefragment.utils.FragmentManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Objects;

/**
 * @author cinder
 */
@Slf4j
@Data
@AllArgsConstructor
public class EncryptTask implements Runnable{
  private FragmentFile file;
  private TimerCollector timerCollector;

  @Override
  public void run() {
    if (Objects.nonNull(timerCollector.getEncryptTimerCollector())) {
      timerCollector.getEncryptTimerCollector().getStartTime().set(file.getIndex(),System.currentTimeMillis());
    }


    byte[] data = file.getData();
    String password = file.getSecretKey();

    // 加密操作逻辑
    // 创建对称密钥
    Key secretKey = new SecretKeySpec(password.getBytes(), "AES");

    // 创建加密器
    Cipher cipher = null;
    try {
      cipher = Cipher.getInstance("AES");
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);

      // 执行加密操作
      byte[] result = cipher.doFinal(data);
      file.setData(data);
      FragmentManager.addPart(file,timerCollector);
      if (Objects.nonNull(timerCollector.getEncryptTimerCollector())) {
        Long start = timerCollector.getEncryptTimerCollector().getStartTime().get(file.getIndex());
        timerCollector.getEncryptTimerCollector().getTotalTime().set(file.getIndex(),System.currentTimeMillis()-start);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
