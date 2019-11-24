package sample.models;

import java.security.PrivateKey;
import java.security.PublicKey;

public  class DecodedKeys {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public DecodedKeys(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }


    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}




