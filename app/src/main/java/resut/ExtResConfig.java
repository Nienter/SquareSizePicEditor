package resut;


import square.size.editor.BuildConfig;

public class ExtResConfig {
    public static byte[] resKey = BuildConfig.RESKEY.getBytes();//BuildConfig.RK;

    public String orgAssetsDir = "assets";
    public String orgResDir = "res";
    public String orgResDirPath = orgResDir+"/";

    public String encArscFn = "r96";//BuildConfig.RE;
    public String encArsc = orgAssetsDir + "/" + encArscFn;

    public String encResFn = "r93";
    public String encResDir = orgAssetsDir + "/" + encResFn;
    public String encResDirPath = encResDir + "/";

    public void update(String orgAssetsDir, String orgResDir, String encArscFn, String encResFn){
        this.orgAssetsDir = orgAssetsDir;
        this.orgResDir = orgResDir;
        this.encArscFn = encArscFn;
        this.encResFn = encResFn;

        orgResDirPath = this.orgResDir+"/";
        encArsc = this.orgAssetsDir + "/" + this.encArscFn;
        encResDir = this.orgAssetsDir + "/" + this.encResFn;
        encResDirPath = this.encResDir + "/";
    }

    public void updateValid(String orgAssetsDir, String orgResDir, String encArscFn, String encResFn){
        this.orgAssetsDir = orgAssetsDir != null ? orgAssetsDir.length() > 0 ? orgAssetsDir : this.orgAssetsDir : this.orgAssetsDir;
        this.orgResDir = orgResDir != null ? orgResDir.length() > 0 ? orgResDir : this.orgResDir : this.orgResDir;
        this.encArscFn = encArscFn != null ? encArscFn.length() > 0 ? encArscFn : this.encArscFn : this.encArscFn;
        this.encResFn = encResFn != null ? encResFn.length() > 0 ? encResFn : this.encResFn : this.encResFn;

        orgResDirPath = this.orgResDir+"/";
        encArsc = this.orgAssetsDir + "/" + this.encArscFn;
        encResDir = this.orgAssetsDir + "/" + this.encResFn;
        encResDirPath = this.encResDir + "/";
    }
}
