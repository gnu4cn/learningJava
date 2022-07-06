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
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;

public class SNMPv3_Demo {
    final static OID[] columnOids = new OID[] {
        new OID("1.3.6.1.2.1.1.3.0"),               // 运行时间？
            new OID("1.3.6.1.2.1.1.5.0"),           // UPS 名称
            new OID("1.3.6.1.2.1.33.1.3.3.1.2.1"),  // U1 工频
            new OID("1.3.6.1.2.1.33.1.3.3.1.2.2"),  // V1 工频
            new OID("1.3.6.1.2.1.33.1.3.3.1.2.3"),  // W1 工频
            new OID("1.3.6.1.2.1.33.1.3.3.1.3.1"),  // U1 电压
            new OID("1.3.6.1.2.1.33.1.3.3.1.3.2"),  // V1 电压
            new OID("1.3.6.1.2.1.33.1.3.3.1.3.3"),  // W1 电压
            new OID("1.3.6.1.2.1.33.1.5.1.0"),      // U2-V2-W2 工频
            new OID("1.3.6.1.2.1.33.1.5.3.1.2.1"),  // U2 电压
            new OID("1.3.6.1.2.1.33.1.5.3.1.2.1"),  // V2 电压
            new OID("1.3.6.1.2.1.33.1.5.3.1.2.1")   // W2 电压
    };

    final static String nmsAdmin = System.getenv("SNMP_ADMIN");
    final static String authKey = System.getenv("SNMP_AUTH_KEY");
    final static String privKey = System.getenv("SNMP_PRIV_KEY");

    public static void main(String[] args) throws IOException, InterruptedException {

        OctetString localEngineID = new OctetString(MPv3.createLocalEngineID());
        Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
        //
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

        UserTarget target = new UserTarget();
        target.setVersion(SnmpConstants.version3);
        target.setAddress(new UdpAddress("10.12.10.108/161"));
        target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
        target.setSecurityName(new OctetString(nmsAdmin));
        target.setTimeout(3000);	//3s
        target.setRetries(3);

        OctetString contextEngineId = new OctetString("0002651100[02]");
        // OctetString contextEngineId = new OctetString();
        sendRequest(snmp, createGetPdu(contextEngineId), target);
        snmpWalk(snmp, target, contextEngineId);
    }

    private static PDU createGetPdu(OctetString contextEngineId) {
        ScopedPDU pdu = new ScopedPDU();
        pdu.setType(PDU.GET);
        pdu.setContextEngineID(contextEngineId);	//if not set, will be SNMP engine id
        //pdu.setContextName(contextName);  //must be same as SNMP agent

        for ( OID oid: columnOids ){
            pdu.add(new VariableBinding(oid));	//sysUpTime
        }

        return pdu;
    }

    private static void sendRequest(Snmp snmp, PDU pdu, UserTarget target)
            throws IOException {
            ResponseEvent responseEvent = snmp.send(pdu, target);
            PDU response = responseEvent.getResponse();

            if (response == null) {
                System.out.println("TimeOut...");
            } else {
                if (response.getErrorStatus() == PDU.noError) {
                    List<? extends VariableBinding> vbs = response.getVariableBindings();
                    // Vector <? extends VariableBinding> vbs = response.getVariableBindings();
                    for (VariableBinding vb : vbs) {
                        System.out.println(vb + " ," + vb.getVariable().getSyntaxString());
                    }
                } else {
                    System.out.println("Error:" + response.getErrorStatusText());
                }
            }
    }

    private static void snmpWalk(Snmp snmp, UserTarget target, OctetString contextEngineId) {
        TableUtils utils = new TableUtils(snmp,
                new MyDefaultPDUFactory(PDU.GETNEXT, //GETNEXT or GETBULK)
                contextEngineId));
        utils.setMaxNumRowsPerPDU(5);	//only for GETBULK, set max-repetitions, default is 10

        // If not null, all returned rows have an index in a range (lowerBoundIndex, upperBoundIndex]
        List<TableEvent> l = utils.getTable(target, columnOids, new OID("3"), new OID("10"));
        for (TableEvent e : l) {
            System.out.println(e);
            System.out.println("已连接上？");
        }
    }

    private static class MyDefaultPDUFactory extends DefaultPDUFactory {
        private OctetString contextEngineId = null;

        public MyDefaultPDUFactory(int pduType, OctetString contextEngineId) {
            super(pduType);
            this.contextEngineId = contextEngineId;
        }

        @Override
        public PDU createPDU(Target target) {
            PDU pdu = super.createPDU(target);
            if (target.getVersion() == SnmpConstants.version3) {
                ((ScopedPDU)pdu).setContextEngineID(contextEngineId);
            }
            return pdu;
        }		
    }
}
