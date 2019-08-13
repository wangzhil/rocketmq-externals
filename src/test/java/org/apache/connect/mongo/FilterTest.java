package org.apache.connect.mongo;

import com.alibaba.fastjson.JSONObject;
import org.apache.connect.mongo.initsync.CollectionMeta;
import org.apache.connect.mongo.replicator.Filter;
import org.apache.connect.mongo.replicator.event.OperationType;
import org.apache.connect.mongo.replicator.event.ReplicationEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterTest {


    private SourceTaskConfig sourceTaskConfig;
    private Map<String, List<String>> insterest;

    @Before
    public void init() {
        sourceTaskConfig = new SourceTaskConfig();
        insterest = new HashMap<>();
    }

    @Test
    public void testSpecialDb() {
        List<String> collections = new ArrayList<>();
        collections.add("person");
        insterest.put("test", collections);
        sourceTaskConfig.setInterestDbAndCollection(JSONObject.toJSONString(insterest));
        Filter filter = new Filter(sourceTaskConfig);
        Assert.assertTrue(filter.filterMeta(new CollectionMeta("test", "person")));
        Assert.assertFalse(filter.filterMeta(new CollectionMeta("test", "person01")));
    }


    @Test
    public void testBlankDb() {
        Filter filter = new Filter(sourceTaskConfig);
        Assert.assertTrue(filter.filterMeta(new CollectionMeta("test", "test")));
        Assert.assertTrue(filter.filterMeta(new CollectionMeta("test1", "test01")));
    }


    @Test
    public void testAsterisk() {
        List<String> collections = new ArrayList<>();
        collections.add("*");
        insterest.put("test", collections);
        sourceTaskConfig.setInterestDbAndCollection(JSONObject.toJSONString(insterest));
        Filter filter = new Filter(sourceTaskConfig);
        Assert.assertTrue(filter.filterMeta(new CollectionMeta("test", "testsad")));
        Assert.assertTrue(filter.filterMeta(new CollectionMeta("test", "tests032")));
    }



    @Test
    public void testFilterEvent() {
        Filter filter = new Filter(sourceTaskConfig);
        ReplicationEvent replicationEvent = new ReplicationEvent();
        replicationEvent.setOperationType(OperationType.NOOP);
        Assert.assertFalse(filter.filterEvent(replicationEvent));
        replicationEvent.setOperationType(OperationType.DBCOMMAND);
        Assert.assertTrue(filter.filterEvent(replicationEvent));
    }


}