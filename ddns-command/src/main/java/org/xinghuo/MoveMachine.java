package org.xinghuo;

import com.aliyuncs.IAcsClient;
import org.xinghuo.ddns.service.DdnsService;
import org.xinghuo.ddns.service.impl.DdnsServiceImpl;

import java.util.HashSet;
import java.util.Set;

public class MoveMachine extends ListNoDns {
    protected static Set<String> resultMoveIp = new HashSet<>();

    public MoveMachine(IAcsClient cs) {
        super(cs);
        client = cs;
    }

    public static void main(String[] args) {
        new MoveMachine(ClientFactory.get()).run();
    }

    @Override
    public void run() {
        DdnsService ddnsService = new DdnsServiceImpl(client);
        readSet("result/moveMachineResultDomainIp.txt", resultMoveIp);
        setToListQueue(resultMoveIp).forEach(item -> {
            String[] strings = item.split(" ");
            try {
                if (strings[2].equals("null")) {
                    System.out.println("error " + item);
                }else{
                    ddnsService.updateDdns(strings[1], "", "*", strings[2]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }
}
