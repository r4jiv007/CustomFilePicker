package my.app.browser.utility;

/**
 * Created by Rajiv on 8/28/13.
 */
public class TextUtility {


    /**
     * this methods changes the case to Title Case
     * @param text
     * @return
     */
    public static String toTitleCase(String text){

        int len = text.length();
        StringBuilder sb = new StringBuilder(text.toLowerCase());
        boolean change_case = true;
        char temp='0';
        for(int i=0;i<len;i++ ){
            if(change_case){
                temp=sb.charAt(i);
                if(97<=(int)temp && (int)temp <=122 ){
                    sb.deleteCharAt(i);
                    sb.insert(i,(char) (temp-32));
                }
                change_case=false;
            }
            if(sb.charAt(i)==' '){
                change_case=true;
            }

        }
        return sb.toString();
    }
}
