package com.xfoss.Utils;

import java.io.IOException;
import java.util.List;
// import java.util.Vector;

import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPv3API {
    final static String SNMP_USER = System.getenv("SNMP_ADMIN");
    final static String AUTH_KEY = System.getenv("SNMP_AUTH_KEY");
    final static String PRIV_KEY = System.getenv("SNMP_PRIV_KEY");

    final static String[] columnOids = new String[] {
        "1.3.6.1.2.1.1.3.0",               // 运行时间？
            "1.3.6.1.2.1.1.5.0",           // UPS 名称
            "1.3.6.1.2.1.33.1.3.3.1.2.1",  // U1 工频
            "1.3.6.1.2.1.33.1.3.3.1.2.2",  // V1 工频
            "1.3.6.1.2.1.33.1.3.3.1.2.3",  // W1 工频
            "1.3.6.1.2.1.33.1.3.3.1.3.1",  // U1 电压
            "1.3.6.1.2.1.33.1.3.3.1.3.2",  // V1 电压
            "1.3.6.1.2.1.33.1.3.3.1.3.3",  // W1 电压
            "1.3.6.1.2.1.33.1.5.1.0",      // U2-V2-W2 工频
            "1.3.6.1.2.1.33.1.5.3.1.2.1",  // U2 电压
            "1.3.6.1.2.1.33.1.5.3.1.2.1",  // V2 电压
            "1.3.6.1.2.1.33.1.5.3.1.2.1"   // W2 电压
    };

    public static void main(String[] args) {
        for (String oid: columnOids) {
            String result = sendRequest(
                    "10.12.10.108", 
                    "161", 
                    oid,
                    SNMP_USER,
                    AUTH_KEY,
                    PRIV_KEY);
            System.out.println(result);
        }
    }

    public static String sendRequest(
            String hostIp, 
            String portNo, 
            String columnOid, 
            String securityName, 
            String authKey, 
            String privKey
            ) 
    {
        PDU response = new PDU();
        try {
            Snmp snmp = snmpInit(securityName, authKey, privKey);
            UserTarget target = targetInit(hostIp, portNo, securityName);
            PDU pdu = createGetPdu(columnOid);
            ResponseEvent responseEvent = snmp.send(pdu, target);

            response = responseEvent.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: 连接 SNMP 对象时失败/Failed to connect to the SNMP server.";
        }

        if (response == null) {
            return new String("Error: 获取 SNMP 数据失败/Failed to fetch SNMP OID data.");
        } else {
            if (response.getErrorStatus() == PDU.noError) {
                List<? extends VariableBinding> vbs = response.getVariableBindings();
                // Vector <? extends VariableBinding> vbs = response.getVariableBindings();
                for (VariableBinding vb : vbs) {
                    return vb.getVariable().toString();
                }
            } else {
                return String.format("Error: %s", response.getErrorStatusText());
            }
        }

        return "Complete";
    }

    private static UserTarget targetInit(String hostIp, String port, String securityName) {

        UserTarget target = new UserTarget();
        target.setVersion(SnmpConstants.version3);
        target.setAddress(new UdpAddress(String.format("%s/%s", hostIp, port)));
        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        target.setSecurityName(new OctetString(securityName));
        target.setTimeout(3000);	//3s
        target.setRetries(3);

        return target;
    }

    private static Snmp snmpInit(String securityName, String authKey, String privKey) throws IOException, InterruptedException {

        OctetString localEngineID = new OctetString(MPv3.createLocalEngineID());
        Snmp snmp = new Snmp(new DefaultUdpTransportMapping());

        // https://stackoverflow.com/questions/57273315/snmp4j-client-gives-unsupported-security-level
        //
        // In SNMP4J 3.x the security protocol AuthSHA (and AuthMD5) are no longer added
        // by default to the static SecurityProtocols instance because they are now considered as unsafe.
        SecurityProtocols.getInstance().addAuthenticationProtocol(new AuthSHA());
        USM usm = new USM(SecurityProtocols.getInstance(), localEngineID, 0);

        SecurityModels.getInstance().addSecurityModel(usm);
        snmp.listen();


        // Add User
        UsmUser user = new UsmUser(
                new OctetString(securityName),
                AuthSHA.ID, new OctetString(authKey),
                PrivDES.ID, new OctetString(privKey)
                );
        //If the specified SNMP engine id is specified, this user can only be used with the specified engine ID
        //So if it's not correct, will get an error that can't find a user from the user table.
        //snmp.getUSM().addUser(new OctetString("nmsAdmin"), new OctetString("0002651100"), user);
        snmp.getUSM().addUser(new OctetString(securityName), user);

        return snmp;
    }

    private static PDU createGetPdu(String columnOid) {

        // OctetString contextEngineId = new OctetString("0002651100[02]");
        OctetString contextEngineId = new OctetString();

        ScopedPDU pdu = new ScopedPDU();
        pdu.setType(PDU.GET);
        pdu.setContextEngineID(contextEngineId);	//if not set, will be SNMP engine id
        //pdu.setContextName(contextName);  //must be same as SNMP agent

        pdu.add(new VariableBinding(new OID(columnOid)));	//sysUpTime

        return pdu;
    }
}
