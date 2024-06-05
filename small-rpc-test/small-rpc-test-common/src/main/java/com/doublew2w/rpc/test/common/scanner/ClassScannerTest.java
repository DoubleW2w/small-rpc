package com.doublew2w.rpc.test.common.scanner;

import com.doublew2w.rpc.common.scanner.ClassScanner;
import com.doublew2w.rpc.common.scanner.reference.RpcReferenceScanner;
import com.doublew2w.rpc.common.scanner.server.RpcServiceScanner;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * ClassScanner 类扫描器测试类
 *
 * @author: DoubleW2w
 * @date: 2024/6/5 23:55
 * @project: small-rpc
 */
public class ClassScannerTest {
  @Test
  public void testScannerClassNameList() throws Exception {
    List<String> classNameList =
        ClassScanner.getClassNameList("com.doublew2w.rpc.test.common.scanner");
    classNameList.forEach(System.out::println);
  }

  /** 扫描io.binghe.rpc.test.scanner包下所有标注了@RpcService注解的类 */
  @Test
  public void testScannerRpcServiceClassNameList() throws Exception {
    RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(
        "com.doublew2w.rpc.test.common.scanner");
  }

  /** 扫描io.binghe.rpc.test.scanner包下所有标注了@RpcReference注解的类 */
  @Test
  public void testScannerRpcReferenceClassNameList() throws Exception {
    RpcReferenceScanner.doScannerWithRpcReferenceAnnotationFilter(
        "com.doublew2w.rpc.test.common.scanner");
  }
}
