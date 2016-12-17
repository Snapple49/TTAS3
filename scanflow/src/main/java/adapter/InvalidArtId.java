package scanflow.adapter;

/**
 * Created by matheusandrade on 12/8/16.
 */

public enum InvalidArtId {
	InvalidArtId1("12313"),
	InvalidArtId2("22"),
	InvalidArtId3("-1"),
	InvalidArtId4("0");

	String value;

	InvalidArtId(String value) {
        this.value = value;
    }

    public String value(){
    	return this.value;
    }


}