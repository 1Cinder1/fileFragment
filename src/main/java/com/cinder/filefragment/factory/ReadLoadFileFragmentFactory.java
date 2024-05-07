package com.cinder.filefragment.factory;

/**
 * @author cinder
 */
public class ReadLoadFileFragmentFactory extends AbstractFileFragmentFactory{
  @Override
  AbstractFileFragment newInstance(String str) {
    return new ReadLoadFileFragment(super.timerCollector);
  }
}
