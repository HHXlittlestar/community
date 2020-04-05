package com.huhx.community.service;

import com.huhx.community.dto.NotificationDTO;
import com.huhx.community.dto.PageInfoDTO;
import com.huhx.community.enums.NotificationStatusEnum;
import com.huhx.community.enums.NotificationTypeEnum;
import com.huhx.community.exception.CustomizeErrorCode;
import com.huhx.community.exception.CustomizeException;
import com.huhx.community.mapper.NotificationMapper;
import com.huhx.community.model.Notification;
import com.huhx.community.model.NotificationExample;
import com.huhx.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    PageInfoDTO getPageInfoDTO( List<Notification> notifications){
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        PageInfoDTO<NotificationDTO> pageInfoDTO = new PageInfoDTO<>();
        pageInfoDTO.setDatas(notificationDTOS);
        return pageInfoDTO;
    }

    //分页查找出所有的未读消息
    public PageInfoDTO list(Long userId, Integer page, Integer size) {
        int starIndex = (page - 1) * size;
        NotificationExample example = new NotificationExample();
        example.createCriteria().andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_creat desc");
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, new RowBounds(starIndex, size));

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);

        PageInfoDTO pageInfoDTO = getPageInfoDTO(notifications);
        pageInfoDTO.setPageInfo(totalCount, page, size);
        return pageInfoDTO;
    }

    //返回未读消息的数量
    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    //读取通知
    public NotificationDTO read(Long id, User user) {
        //先根据id从数据库中查找出这个通知
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null) {
            //通知找不到了
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            //当前用户id与被通知者id不等，则无法读取
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        //读取
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        //回复了通知or回复了文章
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }
}
