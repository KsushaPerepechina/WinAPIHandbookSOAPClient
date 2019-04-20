import org.apache.ws.axis2.WinAPIHandbookServiceStub.WinAPIFunction;

interface ProtocolPerformer {
    void connect(String address);
    void insert(WinAPIFunction func);
    WinAPIFunction[] getAllFunctions();
    void update(WinAPIFunction func);
    void delete(WinAPIFunction func);
}