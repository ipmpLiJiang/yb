package cc.mrbird.febs.com.service;

import cc.mrbird.febs.com.entity.ComSms;
import com.baomidou.mybatisplus.extension.service.IService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import cc.mrbird.febs.common.domain.QueryRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author viki
 * @since 2020-11-13
 */
public interface IComSmsService extends IService<ComSms> {

        IPage<ComSms> findComSmss(QueryRequest request, ComSms comSms);

        IPage<ComSms> findComSmsList(QueryRequest request, ComSms comSms);

        void createComSms(ComSms comSms);

        void updateComSms(ComSms comSms);

        void deleteComSmss(String[]Ids);

        List<ComSms> findLmdSmsList(ComSms comSms);

        List<ComSms> findSmsList(ComSms comSms);

        List<ComSms> findSmsTopLists(ComSms comSms);

        void sendSms();

        void sendSmsService(ArrayList<String> personCodeList, int sendType, String sendContent, Long uId, String Uname);

        void sendSmsService(String personCode, int sendType, String sendContent, Long uId, String Uname);
}
