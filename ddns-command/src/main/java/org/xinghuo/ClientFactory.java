package org.xinghuo;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientFactory {
    public static IAcsClient get() {
        String regionId = PropertiesUtils.get("regionId").trim();
        String accessKeyId = PropertiesUtils.get("accessKeyId").trim();
        String accessKeySecret = PropertiesUtils.get("accessKeySecret").trim();
        IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        return new DefaultAcsClient(profile);
    }
}
