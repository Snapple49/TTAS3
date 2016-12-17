package scanflow.adapter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class ScanflowInterface {

    private Socket socket;
    private BufferedReader input;
    private DataOutputStream output;
    private boolean connected;
    public static boolean DEBUG = true;

    public ScanflowInterface() {
    	connected = false;
        try {
            this.socket = new Socket("sid.cs.ru.nl", 25999);
            this.socket.setSoTimeout(2000);
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.output = new DataOutputStream(this.socket.getOutputStream());
            if (input.readLine().equals("220 SCRP Service ready")){
            	System.out.println("Successfully connected to server");
            	connected = true;
            }
        } catch (Exception e) {
            System.out.println("Failed trying to open socket:" + e);
        }
    }

    public boolean interfaceIsConnected(){
    	return this.connected;
    }
    
    private List<ResponseCode> runCommand(String command) {
        return runCommand(command, 1);
    }

    private List<ResponseCode> runCommand(String command, int lines) {
        List<String> responses = new ArrayList<>();
        try {
        	if(lines <= 0){
        		this.output.writeBytes(command);
        		this.output.flush();
        	}else{
        		this.output.writeBytes(command);
        		responses = flushLines(lines);

        		if (DEBUG) {
        			System.out.println(command);
        			System.out.println(responses);
        		}
        	}
        } catch (Exception e) {
            System.out.println("Erro1: " + e);
        }
        return stringsToResponses(responses);
    }

    private List<String> flushLines(int lines){
        List<String> responses = new ArrayList<>();
        for (int i = 0; i < lines; i++){
            try {
                responses.add(this.input.readLine());
            }
            catch (SocketTimeoutException te){

            }
            catch (IOException e) {
                System.out.println("Erro2: " + e);
            }
        }
        return responses;
    }

    public List<ResponseCode> signOn(int id, String password) {
        String command = String.format("signon %d:%s\n", id, password);
        return runCommand(command);
    }

    public List<ResponseCode> signOff() {
        String command = "signoff\n";
        return runCommand(command);
    }

    public List<ResponseCode> getVariable(String variable) {
        String command = String.format("get %s\n", variable);
        return runCommand(command);
    }

    public List<ResponseCode> artId(String id) {
        String command = String.format("artid %s\n", id);
        return runCommand(command);
    }

    public List<ResponseCode> artReg(String id) {
        return artReg(id, 1);
    }

    public List<ResponseCode> artReg(String id, int amount) {
        String command = String.format("artreg %s:%d\n", id, amount);
        return runCommand(command, 2);
    }

    public List<ResponseCode> artRegEmpty() {
        String command = "artreg\n";
        return runCommand(command);
    }

    public List<ResponseCode> trans(String method) {
        String command = String.format("trans %s\n", method);
        return runCommand(command);
    }

    public List<ResponseCode> trans(String method, float amount, float cents) {
        String command = String.format("trans %s:%f,%f\n", method, amount, cents);
        return runCommand(command);
    }

    public List<ResponseCode> idle() {
        String command = "idle\n";
        return runCommand(command);
    }

    public List<ResponseCode> openAccount() {
        String command = "open\n";
        return runCommand(command);
    }

    public List<ResponseCode> openAccount(int account) {
        String command = String.format("open %d\n");
        return runCommand(command);
    }

    public List<ResponseCode> closeAccount() {
        String command = "close\n";
        return runCommand(command);
    }

    public List<ResponseCode> accountState() {
        return getVariable("CS_ACCNT");
    }

    public void quit() {
    	String command = "signoff\n"; 
    	runCommand(command);
    	command = "quit\n";
    	runCommand(command, 0);  
    	this.connected = false;
    }

    private List<ResponseCode> stringsToResponses(List<String> strings) {
        List<ResponseCode> responses = new ArrayList<>();
        for (String s : strings) {
            responses.add(stringToResponse(s));
        }
        return responses;
    }

    private ResponseCode stringToResponse(String s) {
        int code = Integer.parseInt(s.substring(0, 3));

        switch (code) {
            case 201:
                return ResponseCode.ResumedOperation;
            case 202:
                return ResponseCode.CashRegisterRestored;
            case 210:
                return ResponseCode.CRVariable;
            case 211:
                return ResponseCode.ArtDescription1;
            case 212:
                return ResponseCode.AccountBalance;
            case 213:
                return ResponseCode.ArtDescription2;
            case 214:
                return ResponseCode.Weight;
            case 220:
                return ResponseCode.ServiceReady;
            case 221:
                return ResponseCode.ServiceTerminating;
            case 230:
                return ResponseCode.AccountClosed;
            case 231:
                return ResponseCode.AccountOpened;
            case 232:
                return ResponseCode.ArtRegistered;
            case 233:
                return ResponseCode.AccountIdled;
            case 240:
                return ResponseCode.TransactionSucceeded;
            case 250:
                return ResponseCode.SignedOff;
            case 251:
                return ResponseCode.SignedOn;
            case 260:
                return ResponseCode.DataPrinted;
            case 261:
                return ResponseCode.HTMLText;
            case 450:
                return ResponseCode.SigningRejected;
            case 500:
                return ResponseCode.UnknownCommand;
            case 501:
                return ResponseCode.SyntaxError;
            case 502:
                return ResponseCode.CommandFailed;
            case 503:
                return ResponseCode.ErrorState;
            case 504:
                return ResponseCode.WeighingNotAvailable;
            case 510:
                return ResponseCode.NoSuchVariable;
            case 511:
                return ResponseCode.NoSuchArticle;
            case 512:
                return ResponseCode.NoStableWeight;
            case 530:
                return ResponseCode.NoSuchAccount;
            case 531:
                return ResponseCode.InValidAccountState;
            case 540:
                return ResponseCode.NoSuchTransactionMethod;
            case 541:
                return ResponseCode.BusyTransacting;
            case 542:
                return ResponseCode.TransactionFailed;
            case 550:
                return ResponseCode.NotSignedOn;
            case 551:
                return ResponseCode.AuthenticationFailed;
            case 560:
                return ResponseCode.CRPrintingInactive;
            case 561:
                return ResponseCode.SFUPrintingInactive;

            case 999:
                return ResponseCode.Others;

            default:
                throw new IllegalArgumentException("Invalid response Code");
        }
    }

}
