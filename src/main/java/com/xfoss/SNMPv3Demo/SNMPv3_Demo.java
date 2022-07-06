package com.xfoss.SNMPv3Demo;

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

public class SNMPv3_Demo {
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
        sendRequest("10.12.10.108", "161", columnOids);
    }

    private static void sendRequest(String hostIp, String portNo, String[] columnOids)
        {
            PDU response = new PDU();
            try {
                Snmp snmp = snmpInit();
                UserTarget target = targetInit(hostIp, portNo);
                PDU pdu = createGetPdu(columnOids);
                ResponseEvent responseEvent = snmp.send(pdu, target);

                response = responseEvent.getResponse();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response == null) {
                System.out.println("TimeOut...");
            } else {
                if (response.getErrorStatus() == PDU.noError) {
                    List<? extends VariableBinding> vbs = response.getVariableBindings();
                    // Vector <? extends VariableBinding> vbs = response.getVariableBindings();
                    for (VariableBinding vb : vbs) {
                        System.out.format("%s, %s, %s\n", vb, vb.getVariable().getSyntaxString(), vb.getVariable());
                    }
                } else {
                    System.out.println("Error:" + response.getErrorStatusText());
                }
            }
        }

    private static UserTarget targetInit(String hostIp, String port) {
        String nmsAdmin = System.getenv("SNMP_ADMIN");

        UserTarget target = new UserTarget();
        target.setVersion(SnmpConstants.version3);
        target.setAddress(new UdpAddress(String.format("%s/%s", hostIp, port)));
        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        target.setSecurityName(new OctetString(nmsAdmin));
        target.setTimeout(3000);	//3s
        target.setRetries(3);

        return target;
    }

    private static Snmp snmpInit() throws IOException, InterruptedException {
        String nmsAdmin = System.getenv("SNMP_ADMIN");
        String authKey = System.getenv("SNMP_AUTH_KEY");
        String privKey = System.getenv("SNMP_PRIV_KEY");


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
                new OctetString(nmsAdmin),
                AuthSHA.ID, new OctetString(authKey),
                PrivDES.ID, new OctetString(privKey)
                );
        //If the specified SNMP engine id is specified, this user can only be used with the specified engine ID
        //So if it's not correct, will get an error that can't find a user from the user table.
        //snmp.getUSM().addUser(new OctetString("nmsAdmin"), new OctetString("0002651100"), user);
        snmp.getUSM().addUser(new OctetString(nmsAdmin), user);

        return snmp;
    }

    private static PDU createGetPdu(String[] columnOids) {

        // OctetString contextEngineId = new OctetString("0002651100[02]");
        OctetString contextEngineId = new OctetString();

        ScopedPDU pdu = new ScopedPDU();
        pdu.setType(PDU.GET);
        pdu.setContextEngineID(contextEngineId);	//if not set, will be SNMP engine id
        //pdu.setContextName(contextName);  //must be same as SNMP agent

        for ( String oid: columnOids ){
            pdu.add(new VariableBinding(new OID(oid)));	//sysUpTime
        }

        return pdu;
    }

}
