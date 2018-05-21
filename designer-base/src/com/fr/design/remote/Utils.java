package com.fr.design.remote;

import com.fr.base.FRContext;
import com.fr.decision.webservice.bean.user.UserAdditionBean;
import com.fr.decision.webservice.v10.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Utils {


    private Utils() {
    }

    public static Collection<? extends RemoteMember> getRemoteMember(String keyword) {

        List<UserAdditionBean> userBeans = new ArrayList<>();
        try {
            Map<String, Object> result = UserService.getInstance().getAllUsers(FRContext.getCurrentEnv().getUser(), 1, 10, keyword, "", true);
            userBeans = (List<UserAdditionBean>) result.get("items");
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<RemoteMember> res = new ArrayList<>();

        for (UserAdditionBean userBean : userBeans) {
            res.add(new RemoteMember(userBean.getUsername())
                    .realName(userBean.getRealName())
                    .userId(userBean.getId())
            );
        }
        return res;
    }
}
