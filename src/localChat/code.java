
package localChat;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author g-T-m
 */
public class code extends Agent {
    box p;
    box2 mp;
    login_box box;
    Map hosts;
    @Override
    protected void setup() {
        hosts = new HashMap();
        if("master".equals(getAID().getLocalName().toString())) {
            box=new login_box();
            box.submit.addActionListener(new log_action());

            mp=new box2();
            
            mp.mf.setEditable(false);
            mp.setTitle("M@$tEr");
            mp.mb1.addActionListener(new maction1());
            mp.mb2.addActionListener(new maction2());
            mp.mb3.addActionListener(new maction3());

        }
        
        else {
            p=new box();
            
            ACLMessage m=new ACLMessage(ACLMessage.REQUEST);
            m.setContent("Invoke");
            AID n=new AID("master@127.0.0.1:1099/JADE",AID.ISGUID);
            n.addAddresses("http://127.0.0.1:7778/acc");
            m.addReceiver(n);
            send(m);
            
            hosts.put("master", "127.0.0.1");
            p.f.setEditable(false);
            p.b.addActionListener(new action());
        }
        
        if("master".equals(getAID().getLocalName().toString())) {
            addBehaviour(new CyclicBehaviour(this) {
                @Override
                public void action() {
                    MessageTemplate mt1 = MessageTemplate.MatchPerformative( ACLMessage.REQUEST );                
                    ACLMessage msg1 = receive(mt1);
                    if(msg1 != null) {
                        String sender = msg1.getSender().getName().toString();
                        String sender_name = sender.substring(0, sender.indexOf("@"));
                        String sender_ip = sender.substring(sender.indexOf("@")+1, sender.indexOf(":"));
            
                        hosts.put(sender_name, sender_ip);
                        
                        mp.a.append(sender_name+"("+hosts.get(sender_name)+"): "+msg1.getContent().toString()+"\n");
                    }
                }
            });
        }
        else {
            addBehaviour(new CyclicBehaviour(this) {
                @Override
                public void action() {
                    MessageTemplate mt2 = MessageTemplate.MatchPerformative( ACLMessage.PROPOSE );                
                    ACLMessage msg2 = receive(mt2);
                    if(msg2 != null) {
                        if("master".equals(msg2.getSender().getLocalName().toString())) {
                            if("End".equals(msg2.getContent().toString())) {

                                p.dispose();
                                doDelete();
                            }
                            else if("Go".equals(msg2.getContent().toString())) {

                                p.f.setEditable(true);
                                p.setTitle(getAID().getName());
                            }
                            else if(msg2.getContent().toString().contains("Add host")) {

                                String agents = msg2.getContent().toString().substring(9);
                                String agent_names="", temp, agent_addr;
                                for(int i=0; i<agents.length(); i++) {
                                    temp = "";
                                    while(i<agents.length() && agents.charAt(i) != ' ') {
                                        temp += agents.charAt(i);
                                        i++;
                                    }
                                    i++;
                                    agent_addr = "";
                                    while(i<agents.length() && agents.charAt(i) != '\n') {
                                        agent_addr += agents.charAt(i);
                                        i++;
                                    }
                                    hosts.put(temp, agent_addr);
                                    temp += '\n';

                                    agent_names += temp;
                                }
                                p.chatList.append(agent_names);
                            }
                            else {

                                p.a.append("\t"+msg2.getContent().toString()+"\n");
                            }
                        }
                        else {
                            if(msg2.getSender().getLocalName().equals(getAID().getLocalName().toString())) {
                            
                                p.a.append(msg2.getContent().toString()+"\n");
                            }
                            else {
                                
                                p.a.append("\t"+msg2.getSender().getLocalName()+": "+msg2.getContent().toString()+"\n");
                            }
                        }
                    }
                }
            });
        }
            
    }
    
    protected class action implements ActionListener {      //Chatting among various agents
        @Override
        public void actionPerformed (ActionEvent e) {
            String s=p.f.getText().toString();
            if(!( "".equals(s)  ||  p.f2.getText().equals(getAID().getLocalName()) ) ) {
                
                p.a.append(getAID().getLocalName().toString()+": "+s+"\n");
                
                ACLMessage m=new ACLMessage(ACLMessage.PROPOSE);
                m.setContent(s);
                
                String receiver = p.f2.getText().toString();
                AID n=new AID(receiver+"@"+hosts.get(receiver)+":1099/JADE", AID.ISGUID);
                n.addAddresses("http://"+hosts.get(receiver)+":7778/acc");
                m.addReceiver(n);
                send(m);

                p.f.setText(null);
            }
        }
    }
    
    protected class log_action implements ActionListener {          //for logging in as master
        @Override
        public void actionPerformed (ActionEvent e) {
            
            char[] p;
            int n;
            String s=box.f1.getText();
            p=box.pass.getPassword();
            n=p.length;
            
            box.dispose();
            if("gtm".equals(s) && n==3) {
                if(p[0]=='g' && p[1]=='t' &&  p[2]=='m' ) {
                    
                    mp.mf.setEditable(true);
                }
                else {
                    mp.dispose();
                    doDelete();
                }
            }
            
            else {
                mp.dispose();
                doDelete();
            }
        }
    }
    
    protected class maction1 implements ActionListener {        //Dispose command from master
        @Override
        public void actionPerformed (ActionEvent e) {
            
            ACLMessage m=new ACLMessage(ACLMessage.PROPOSE);
            m.setContent("End");
            String receiver = mp.mf.getText().toString();
            AID n=new AID(receiver+"@"+hosts.get(receiver)+":1099/JADE",AID.ISGUID);
            n.addAddresses("http://"+hosts.get(receiver)+":7778/acc");
            m.addReceiver(n);
            send(m);
            
            mp.mf.setText(null);
        }
    }
     
    protected class maction2 implements ActionListener {        //Invoke command from master
        @Override
        public void actionPerformed (ActionEvent e) {
            ACLMessage m=new ACLMessage(ACLMessage.PROPOSE);
            m.setContent("Go");
            String hostname = mp.mf.getText().toString().trim();
            
            AID n=new AID(hostname+"@"+hosts.get(hostname)+":1099/JADE", AID.ISGUID);
            n.addAddresses("http://"+hosts.get(hostname)+":7778/acc");
            m.addReceiver(n);
            send(m);
            mp.mf.setText(null);
            
            /*Appending the chat List for both new host and older hosts*/
            ACLMessage m1 = new ACLMessage(ACLMessage.PROPOSE);     //message for already added hosts
            ACLMessage m2 = new ACLMessage(ACLMessage.PROPOSE);     //message for new host
            
            m2.addReceiver(n);
            m1.setContent("Add host "+hostname+" "+hosts.get(hostname)+"\n");
            String str = mp.userList.getText();
            
            int i=0;
            String allHost="Add host ";
            while(i<str.length()) {
                String name="";
                while(str.charAt(i) != '\n') {
                    name += str.charAt(i);
                    i++;
                }
                AID host = new AID(name+"@"+hosts.get(name)+":1099/JADE", AID.ISGUID);
                host.addAddresses("http://"+hosts.get(name)+":7778/acc");
                m1.addReceiver(host);
                allHost += name+" "+hosts.get(name)+"\n";
                
                i++;
            }
            send(m1);
            
            m2.setContent(allHost);
            send(m2);
            /*
            mp.a.append(allHost+"\n\n");
            mp.a.append(hostname+" "+hosts.get(hostname)+"\n");
            */
            mp.userList.append(hostname+"\n");      //add the new host to the list of the master
        }
    }
    
    protected class maction3 implements ActionListener {        //Warning command from master
        @Override
        public void actionPerformed (ActionEvent e) {
            
            ACLMessage m=new ACLMessage(ACLMessage.PROPOSE);
            m.setContent("-----You are WARNED of your Actions-----");
            String receiver = mp.mf.getText().toString();
            AID n=new AID(receiver+"@"+hosts.get(receiver)+":1099/JADE", AID.ISGUID);
            n.addAddresses("http://"+hosts.get(receiver)+":7778/acc");
            m.addReceiver(n);
            send(m);
            
            mp.mf.setText(null);
        }
    }    
}
