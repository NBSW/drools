package com.zjhcsoft.rule.engine.component;

import org.apache.commons.codec.digest.DigestUtils;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.definition.KnowledgePackage;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by XuanLubin on 2014/5/8. 14:51
 */
class RuleKnowledgeStore {

    private Logger logger = LoggerFactory.getLogger(RuleKnowledgeStore.class);

    private static Map<String, KnowledgeBase> knowledgeBaseMap = new HashMap<>();

    private static Set<String> knowledgeBaseMap_locks = new HashSet<>();

    private static Map<String, String> knowledgeBaseMap_errors = new HashMap<>();


    private KnowledgeBase getKnowledgeBase(String ruleContent) throws Exception {
        String $shaHex = DigestUtils.sha1Hex(ruleContent.getBytes());
        if (!knowledgeBaseMap_locks.contains($shaHex)) {
            knowledgeBaseMap_locks.add($shaHex);
            logger.debug("未找到该规则  sha1Hex:{}  Start Build RuleBase...", $shaHex);
            try {
                Collection<KnowledgePackage> knowledgePackageCollection = getKnowledgePackages(ruleContent);
                KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
                kbase.addKnowledgePackages(knowledgePackageCollection);
                knowledgeBaseMap.put($shaHex, kbase);
            } catch (Exception e) {
                knowledgeBaseMap_errors.put($shaHex, e.getMessage());
                throw e;
            }
            logger.debug("规则文件初始化完成 {}", $shaHex);
        } else if (knowledgeBaseMap.containsKey($shaHex)) {
            logger.debug("成功找到该规则  sha1Hex:{}", $shaHex);
        } else {
            while (!knowledgeBaseMap.containsKey($shaHex) && !knowledgeBaseMap_errors.containsKey($shaHex)) {
                logger.debug("规则文件 {} 正在加载中 Wait Rule Build..", $shaHex);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (Exception e) {
                    logger.error("InterruptedException {}", e);
                    e.printStackTrace();
                }
            }
        }
        if (knowledgeBaseMap_errors.containsKey($shaHex)) {
            throw new Exception("该规则规则内容有异常 " + knowledgeBaseMap_errors.get($shaHex));
        }
        return knowledgeBaseMap.get($shaHex);
    }

    private Collection<KnowledgePackage> getKnowledgePackages(String ruleContent) throws Exception {
        Resource resource = ResourceFactory.newReaderResource(new StringReader(ruleContent));
        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(resource, ResourceType.DRL);
        Collection<KnowledgePackage> knowledgePackageCollection = knowledgeBuilder.getKnowledgePackages();
        Exception exception = null;
        if (knowledgeBuilder.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            for (KnowledgeBuilderError error : knowledgeBuilder.getErrors()) {
                errorMsg.append(error.getMessage()).append("\n");
            }
            exception = new Exception("规则内容有异常  \n" + errorMsg);
        } else if (knowledgePackageCollection.isEmpty()) {
            exception = new Exception("规则内容有异常  \n未找到可用的package");
        }
        if (null != exception) {
            logger.error("规则内容异常  {}", exception.getMessage());
            throw exception;
        }
        return knowledgePackageCollection;
    }


    public KnowledgeBase updateKnowledgeBase(String scriptRule) throws Exception{
        return getKnowledgeBase(scriptRule);
    }


    public KnowledgeBase queryKnowledgeBase(String scriptRule) throws Exception{
        return getKnowledgeBase(scriptRule);
    }

    public StatefulKnowledgeSession newStatefulKnowledgeSession(String scriptRule) throws Exception{
        return getKnowledgeBase(scriptRule).newStatefulKnowledgeSession();
    }

    public void removeRuleBase(String ruleContent) {
        String $shaHex = DigestUtils.sha1Hex(ruleContent.getBytes());
        knowledgeBaseMap_errors.remove($shaHex);
        knowledgeBaseMap_locks.remove($shaHex);
        knowledgeBaseMap.remove($shaHex);
    }

    public boolean checkRuleValid(String ruleContent) {
        try {
            getKnowledgePackages(ruleContent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public FactType getFactType(String scriptContent, String packageName, String typeName) {
        try {
            return getKnowledgeBase(scriptContent).getFactType(packageName, typeName);
        } catch (Exception e) {
            logger.error("getFactType({}, {}, {}) {}", scriptContent, packageName, typeName, e);
            e.printStackTrace();
        }
        return null;
    }

}
