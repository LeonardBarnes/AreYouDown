package com.areyoudown.areyoudown;

/**
 * Created by Leonard on 2016/09/06.
 */
public class intentions {
    private String intentionname;
    private String id;



    public intentions(String intentionname, String id) {
        this.intentionname = intentionname;
        this.id=id;

    }

    public String getIntentionname() {
        return intentionname;
    }

    public void setIntentionname(String intentionname) {
        this.intentionname = intentionname;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
