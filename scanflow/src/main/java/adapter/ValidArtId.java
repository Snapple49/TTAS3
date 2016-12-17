package scanflow.adapter;

/**
 * Created by matheusandrade on 12/8/16.
 */

public enum ValidArtId {
	Kek("59922827"),
	Pezsgotabl("20013226"),
	Zorp("5998386301215");

	String value;

	ValidArtId(String value) {
        this.value = value;
    }

    public String value(){
    	return this.value;
    }


}