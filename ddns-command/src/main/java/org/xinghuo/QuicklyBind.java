package org.xinghuo;

import com.aliyuncs.IAcsClient;

public class QuicklyBind extends ListNoDns {
    public QuicklyBind(IAcsClient cs) {
        super(cs);
    }

    public static void main(String[] args) {
        System.out.println(args[0]);
        Integer applyCount = 1;
        if (args.length == 1){
            applyCount = Integer.parseInt(args[0]);
        }
        new QuicklyBind(ClientFactory.get()).run(applyCount);

    }

    public void run(Integer applyCount) {
        readSet("allip.txt", allip);
        readSet("unUsedDomain.txt", unUsedDomain);
        readSet("unUsedIp.txt", unUsedIp);
        for (int i = 0; i <applyCount; i++) {
            bindDomainIp();
        }
        SetWrite("unUsedDomain.txt", unUsedDomain);
        SetWrite("unUsedIp.txt", unUsedIp);
    }

}
