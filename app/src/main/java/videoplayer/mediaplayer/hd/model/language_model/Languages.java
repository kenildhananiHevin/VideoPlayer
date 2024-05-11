package videoplayer.mediaplayer.hd.model.language_model;

public class Languages {
    int image;
    String languageName;
    String languageCode;

    public Languages(int image, String languageName, String languageCode) {
        this.languageName = languageName;
        this.languageCode = languageCode;
        this.image = image;
    }


    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
