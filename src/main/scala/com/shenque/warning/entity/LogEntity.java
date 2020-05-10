package com.shenque.warning.entity;

import java.io.Serializable;

/**
 * @author gl
 * @create 2020-04-23 15:32
 * <p>
 * 参数格式：
 * {
 *     "PROJECT_NAME":"newyituan-h5-backend-api",
 *     "ACTION_RULES":"/goods/home-shop-poster",
 *     "PROJECT_ACTION":"newyituan-h5-backend-api/goods/home-shop-poster",
 *     "REQUEST_METHOD":"GET",
 *     "SERVER_NAME":"www.tc.com",
 *     "REDIRECT_URL":"http://www.tc.com:80/goods/home-shop-poster",
 *     "REMOTE_ADDR":"127.0.0.1",
 *     "SERVER_ADDR":"127.0.0.1",
 *     "OVERALL_ELAPSED":592,
 *     "DOCUMENT_ROOT":"D:/wamp/www/newyituan-h5-backend-api/restful/web"
 * }
 */
public class LogEntity implements Serializable {

    //项目名称
    private String PROJECT_NAME;
    //接口
    private String ACTION_RULES;
    //项目名加接口字符串 作为flink key
    private String PROJECT_ACTION;
    //访问页面使用的请求方法
    private String REQUEST_METHOD;
    //当前运行脚本所在的服务器的主机名
    private String SERVER_NAME;
    //接口请求URL
    private String REDIRECT_URL;
    //浏览当前页面的用户 IP 地址
    private String REMOTE_ADDR;
    //当前运行脚本所在的服务器的 IP 地址
    private String SERVER_ADDR;
    //接口总消耗时间,单位:毫秒
    private Long OVERALL_ELAPSED;
    //当前运行脚本所在的文档根目录
    private String DOCUMENT_ROOT;

    @Override
    public String toString() {
        return "LogEntity{" +
                "PROJECT_NAME='" + PROJECT_NAME + '\'' +
                ", ACTION_RULES='" + ACTION_RULES + '\'' +
                ", PROJECT_ACTION='" + PROJECT_ACTION + '\'' +
                ", REQUEST_METHOD='" + REQUEST_METHOD + '\'' +
                ", SERVER_NAME='" + SERVER_NAME + '\'' +
                ", REDIRECT_URL='" + REDIRECT_URL + '\'' +
                ", REMOTE_ADDR='" + REMOTE_ADDR + '\'' +
                ", SERVER_ADDR='" + SERVER_ADDR + '\'' +
                ", OVERALL_ELAPSED=" + OVERALL_ELAPSED +
                ", DOCUMENT_ROOT='" + DOCUMENT_ROOT + '\'' +
                '}';
    }

    public String getPROJECT_NAME() {
        return PROJECT_NAME;
    }

    public void setPROJECT_NAME(String PROJECT_NAME) {
        this.PROJECT_NAME = PROJECT_NAME;
    }

    public String getACTION_RULES() {
        return ACTION_RULES;
    }

    public void setACTION_RULES(String ACTION_RULES) {
        this.ACTION_RULES = ACTION_RULES;
    }

    public String getPROJECT_ACTION() {
        return PROJECT_ACTION;
    }

    public void setPROJECT_ACTION(String PROJECT_ACTION) {
        this.PROJECT_ACTION = PROJECT_ACTION;
    }

    public String getREQUEST_METHOD() {
        return REQUEST_METHOD;
    }

    public void setREQUEST_METHOD(String REQUEST_METHOD) {
        this.REQUEST_METHOD = REQUEST_METHOD;
    }

    public String getSERVER_NAME() {
        return SERVER_NAME;
    }

    public void setSERVER_NAME(String SERVER_NAME) {
        this.SERVER_NAME = SERVER_NAME;
    }

    public String getREDIRECT_URL() {
        return REDIRECT_URL;
    }

    public void setREDIRECT_URL(String REDIRECT_URL) {
        this.REDIRECT_URL = REDIRECT_URL;
    }

    public String getREMOTE_ADDR() {
        return REMOTE_ADDR;
    }

    public void setREMOTE_ADDR(String REMOTE_ADDR) {
        this.REMOTE_ADDR = REMOTE_ADDR;
    }

    public String getSERVER_ADDR() {
        return SERVER_ADDR;
    }

    public void setSERVER_ADDR(String SERVER_ADDR) {
        this.SERVER_ADDR = SERVER_ADDR;
    }

    public Long getOVERALL_ELAPSED() {
        return OVERALL_ELAPSED;
    }

    public void setOVERALL_ELAPSED(Long OVERALL_ELAPSED) {
        this.OVERALL_ELAPSED = OVERALL_ELAPSED;
    }

    public String getDOCUMENT_ROOT() {
        return DOCUMENT_ROOT;
    }

    public void setDOCUMENT_ROOT(String DOCUMENT_ROOT) {
        this.DOCUMENT_ROOT = DOCUMENT_ROOT;
    }
}
