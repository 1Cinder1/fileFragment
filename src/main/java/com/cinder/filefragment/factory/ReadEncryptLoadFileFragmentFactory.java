package com.cinder.filefragment.factory;

/**
 * @author cinder
 */
public class ReadEncryptLoadFileFragmentFactory extends AbstractFileFragmentFactory{

  private static final String AES_ENCRYPT="aes_encrypt";

  @Override
  AbstractFileFragment newInstance(String str) {
    if (str.equals(AES_ENCRYPT)) {
      return new AesEncryptFileFragment(super.timerCollector);
    }
    return null;
  }

}
