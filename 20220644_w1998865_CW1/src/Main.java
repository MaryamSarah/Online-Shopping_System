public class Main {
    public static void main(String[] args) {
        Tree myFavouriteOAKTree=new Tree(25,5,TreeType.OAK);
        System.out.println (myFavouriteOAKTree.treeType);

        Tree myFavouriteMapleTree=new Tree(90,30, TreeType.MAPLE);

        if (myFavouriteOAKTree.heightFt>100){
            System.out.println("That's a tall"+myFavouriteOAKTree+"tree!");
        }

        if (myFavouriteMapleTree.heightFt>100){
            System.out.println("That's a tall"+myFavouriteMapleTree+"tree!");
        }

    }
}