import java.util.ArrayList;

/**
 * @className: ContentsSection
 * @author: Lodisaq
 * @description: 创建目录 以及目录初始化
 * @date: 2022/5/31 19:52
 * @version: 1.0
 */

public class ContentsSection {
    public static void main(String[] args) {
        Content rt = new Content("0");
        Content root = new Content("/");
        root.createContent(rt, "home");
        root.createContent(rt, "boot");
        root.createContent(rt, "lib");
        root.createContent(rt, "bin");
        rt.showContent();
        String string = "home";
        System.out.println("删除" + string);
        root.deleteContent(rt,string);
        rt.showContent();
        System.out.println("将boot改变为program");
        root.changeContent(rt,"boot","program");
        rt.showContent();
    }

}

/**
 * @className: Content
 * @author: Lodisaq
 * @description: 目录类
 * @date: 2022/5/31 19:52
 * @version: 1.0
 */
class Content {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInode() {
        return inode;
    }

    public void setInode(Integer inode) {
        this.inode = inode;
    }

    //目录名字
    private String name;
    //索引节点
    private Integer inode;

    public ArrayList<Content> getSubDirectory() {
        return subDirectory;
    }

    public void setSubDirectory(ArrayList<Content> subDirectory) {
        this.subDirectory = subDirectory;
    }

    //子目录
    private ArrayList<Content> subDirectory = new ArrayList<>();

    public Content(String name) {
        this.name = name;
    }

    //创建目录
    public void createContent(Content content, String name) {
        Content c = new Content(name);
        content.getSubDirectory().add(c);
    }

    //删除某一个子目录目录
    public void deleteContent(Content content,String name){
        int i = 0;
        for (;i<content.getSubDirectory().size();i++){
            if(content.getSubDirectory().get(i).getName().equals(name)) {
                content.getSubDirectory().remove(i);
                i--;
            }
        }
    }

    //改变目录名字
    public void changeContent(Content content, String sName,String name) {
        for(Content c:content.getSubDirectory()){
            if(c.getName().equals(sName)){
                c.setName(name);
            }
        }
    }

    //打印当前目录和其子目录
    public void showContent() {
        System.out.println("*********************************************");
        System.out.println("*当前目录是:" + this.name);
        System.out.println("*子目录为:");
        System.out.print("*");
        for (Content c : this.subDirectory) {
            System.out.print(c.getName() + " ");
        }
        System.out.println();
        System.out.println("*********************************************");
    }


}
