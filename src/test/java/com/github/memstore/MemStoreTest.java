package com.github.memstore;

import com.github.store.EsStoreHandler;
import com.github.store.InvokeContext;
import org.junit.Test;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author winstone
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class MemStoreTest {

    @Test
    public void testSave()throws  Exception{
        EsStoreHandler esStoreHandler = new EsStoreHandler();
        for(int i=0;i<10;i++){
            InvokeContext invokeContext = new InvokeContext();
            invokeContext.setServerId("1"+"-"+i);
            invokeContext.setSlotName("test"+"-"+i);
            esStoreHandler.handle(invokeContext);
        }
        Thread.sleep(20000000);
    }
}
