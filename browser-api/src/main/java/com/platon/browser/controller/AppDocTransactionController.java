package com.platon.browser.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.common.DownFileCommon;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.req.staking.QueryClaimByStakingReq;
import com.platon.browser.req.staking.QueryInnerByAddrReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.staking.QueryClaimByStakingResp;
import com.platon.browser.res.staking.QueryInnerTxByAddrResp;
import com.platon.browser.res.transaction.QueryClaimByAddressResp;
import com.platon.browser.res.transaction.TransactionDetailsResp;
import com.platon.browser.res.transaction.TransactionListResp;
import com.platon.browser.util.I18nUtil;

/**
 * 交易模块Contract。定义使用方法
 * 
 * @file AppDocTransactionController.java
 * @description
 * @author zhangrj
 * @data 2019年8月31日
 */
@RestController
public class AppDocTransactionController implements AppDocTransaction {

    private final Logger logger = LoggerFactory.getLogger(AppDocTransactionController.class);

    @Autowired
    private I18nUtil i18n;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private DownFileCommon downFileCommon;

    @Autowired
    private CommonMethod commonMethod;

    @Override
    public WebAsyncTask<RespPage<TransactionListResp>> transactionList(@Valid PageReq req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<RespPage<TransactionListResp>> webAsyncTask =
            new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> this.transactionService.getTransactionList(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @Override
    public WebAsyncTask<RespPage<TransactionListResp>>
        transactionListByBlock(@Valid TransactionListByBlockRequest req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<RespPage<TransactionListResp>> webAsyncTask =
            new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> this.transactionService.getTransactionListByBlock(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @Override
    public WebAsyncTask<RespPage<TransactionListResp>>
        transactionListByAddress(@Valid TransactionListByAddressRequest req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<RespPage<TransactionListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
            () -> this.transactionService.getTransactionListByAddress(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @Override
    public void addressTransactionDownload(String address, Long date, String local, String timeZone, String token,
        HttpServletResponse response) {
        /**
         * 鉴权
         */
        this.commonMethod.recaptchaAuth(token);
        /**
         * 对地址进行补充前缀
         */
        address = address.toLowerCase();
        AccountDownload accountDownload =
            this.transactionService.transactionListByAddressDownload(address, date, local, timeZone);
        try {
            this.downFileCommon.download(response, accountDownload.getFilename(), accountDownload.getLength(),
                accountDownload.getData());
        } catch (Exception e) {
            this.logger.error(e.getMessage());
            throw new BusinessException(this.i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }

    @Override
    public WebAsyncTask<BaseResp<TransactionDetailsResp>> transactionDetails(@Valid TransactionDetailsReq req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<BaseResp<TransactionDetailsResp>> webAsyncTask =
            new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> {
                TransactionDetailsResp transactionDetailsResp = this.transactionService.transactionDetails(req);
                return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), this.i18n.i(I18nEnum.SUCCESS),
                    transactionDetailsResp);
            });
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @Override
    public WebAsyncTask<RespPage<QueryClaimByAddressResp>>
        queryClaimByAddress(@Valid TransactionListByAddressRequest req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<RespPage<QueryClaimByAddressResp>> webAsyncTask =
            new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> this.transactionService.queryClaimByAddress(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @Override
    public WebAsyncTask<RespPage<QueryClaimByStakingResp>> queryClaimByStaking(@Valid QueryClaimByStakingReq req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<RespPage<QueryClaimByStakingResp>> webAsyncTask =
            new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> this.transactionService.queryClaimByStaking(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

    @Override
    public WebAsyncTask<RespPage<QueryInnerTxByAddrResp>> queryInnerByAddr(@Valid QueryInnerByAddrReq req) {
        /**
         * 异步调用，超时则进入timeout
         */
        WebAsyncTask<RespPage<QueryInnerTxByAddrResp>> webAsyncTask =
            new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> this.transactionService.queryInnerByAddr(req));
        CommonMethod.onTimeOut(webAsyncTask);
        return webAsyncTask;
    }

}
