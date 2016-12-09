package ScanflowInterface;

/**
 * Created by matheusandrade on 12/8/16.
 */

public enum ResponseCode {
    ResumedOperation(201),
    CashRegisterRestored(202),
    CRVariable(210),
    ArtDescription1(211),
    AccountBalance(212),
    ArtDescription2(213),
    Weight(214),
    ServiceReady(220),
    ServiceTerminating(221),
    AccountClosed(230),
    AccountOpened(231),
    ArtRegistered(232),
    AccountIdled(233),
    TransactionSucceeded(240),
    SignedOff(250),
    SignedOn(251),
    DataPrinted(260),
    HTMLText(261),

    //Again
    SigningRejected(450),

    //Fail
    UnknownCommand(500),
    SyntaxError(501),
    CommandFailed(502),
    ErrorState(503),
    WeighingNotAvailable(504),
    NoSuchVariable(510),
    NoSuchArticle(511),
    NoStableWeight(512),
    NoSuchAccount(530),
    InValidAccountState(531),
    NoSuchTransactionMethod(540),
    BusyTransacting(541),
    TransactionFailed(542),
    NotSignedOn(550),
    AuthenticationFailed(551),
    CRPrintingInactive(560),
    SFUPrintingInactive(561),

    Others(999);

    int value;

    ResponseCode(int value) {
        this.value = value;
    }
}