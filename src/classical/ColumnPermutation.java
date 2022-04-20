package classical;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import classical.VigenereCipher;
public class ColumnPermutation {

    static String alpha="ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    //将keyword转化为数字序列
    public static int[] dealKeyword(String key){
        ArrayList<Integer>  arr=new ArrayList<>();
        key=key.toUpperCase(Locale.ROOT);
        int k[]=new int[key.length()];
        for(int i=0;i<key.length();i++){
            arr.add(alpha.indexOf(key.charAt(i)));
        }
        for(int i=0;i<arr.size();i++){
            int index=arr.get(i);
            int flag=1;
            for(int j=0;j<arr.size();j++){
                if(index== arr.get(j)&&i>j) flag++;
                if(index>arr.get(j)) flag++;
            }
            k[i]=flag;
        }
        return k;
    }

    //打乱列的次序
    public static char[][] exchange(char[][] matrix,int[] k){
        int rowNum=matrix.length;
        int colNum=k.length;
        char[][] newmatrix=new char[rowNum][colNum];
        for(int i=0;i<rowNum;i++){
            for(int j=0;j<colNum;j++){
                int index=k[j]-1;
                newmatrix[i][index]=matrix[i][j];
            }
        }
        return newmatrix;
    }

    //将明文转换为矩阵
    public static char[][]  dealP(String P,int colNum){
        P=P.replaceAll(" ","").toUpperCase();//去除空格,并转换成大写
        int flag=P.length()%colNum;
        for(int i=0;i<flag;i++){
            P=P+"&";//进行填充
        }
        int rowNum=P.length()/colNum;
        char[][] matrix=new char[rowNum][colNum];
        int count=0;
        //把消息一行一行地写入矩形块
        for(int i=0;i<rowNum;i++){
            for(int j=0;j<colNum;j++){
                matrix[i][j]=P.charAt(count++);
            }
        }
        return matrix;
    }

    //加密方法
    public static String encrypt(String P,String keyword){
        int[] K=dealKeyword(keyword);
        int colNum=K.length;
        char[][] matrix=dealP(P,colNum);
        //printMatrix(matrix);
        char[][] newMatrix=exchange(matrix,K);
        //printMatrix(newMatrix);
        int rowNum=newMatrix.length;
        StringBuilder sb=new StringBuilder();
        //按列读出
        for(int i=0;i<colNum;i++){
            char[] col=new char[rowNum];
            for(int j=0;j<rowNum;j++){
                col[j]=newMatrix[j][i];
            }
            sb.append(col);
        }
        return sb.toString();
    }

    //处理key
    public static int[] reverseK(int[] K){
        int len=K.length;
        int[] key=new int[len];
        List<Integer> list=new ArrayList<Integer>();
        for(int k: K){
            list.add(k);
        }
        for(int i=0;i<len;i++){
            key[i]=list.indexOf(i+1)+1;
        }
        return key;
    }
    //处理密文
    public static char[][] dealC(String C,int col){
            C=C.replaceAll(" ","").toUpperCase();//去除空格,并转换成大写
            int rowNum=C.length()/col;
            char[][] matrix=new char[rowNum][col];
            int count=0;
            //把密文一列一列地写出矩形块
            for(int i=0;i<col;i++){
                for(int j=0;j<rowNum;j++){
                    matrix[j][i]=C.charAt(count++);
                }
            }
            return matrix;
    }
    //解密方法
    public static String decrypt(String C,String keyword){
        int[] Key=dealKeyword(keyword);
        int[] K=reverseK(Key);
        int colNum=K.length;
        char[][] matrix=dealC(C,colNum);
        char[][] newMatrix=exchange(matrix,K);
        int rowNum=newMatrix.length;
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<rowNum;i++){
            char[] col=new char[colNum];
            for(int j=0;j<colNum;j++){
                col[j]=newMatrix[i][j];
            }
            sb.append(col);
        }
        String result=sb.toString();
        result=result.replaceAll("&","");
        return result;
    }

    //文件的加解密
    //加密文件中内容,并将结果写入文件
    public static String readFileIntoString(String filename) throws IOException {
        filename = "src\\" + filename;
        File file = new File(filename);
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        String result = "";
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                result += str;
            }
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                fileReader.close();
            } catch (Exception e) {

            }
            return result;
        }
    }

    public static void EncryFile(String filename, String keyword) throws IOException {
        String plaintext = readFileIntoString(filename);
        String cipher = encrypt(plaintext, keyword);
        File file = new File("src\\columnCipher.txt");//我们在该类的位置创建一个新文件
        FileWriter fileWriter = null;//创建文件写入对象
        BufferedWriter f1 = null;//创建字符流写入对象
        try {
            //这里把文件写入对象和字符流写入对象分开写了
            fileWriter = new FileWriter("src\\columnCipher.txt");//创建一个名为cc.txt的文件
            f1 = new BufferedWriter(fileWriter);
            //通过循环遍历上面的String 数组中的元素
            for (int i = 0; i < cipher.length(); i++) {
                f1.write(cipher.charAt(i));//把String中的字符写入文件
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            try {
                f1.close();
                fileWriter.close();//关闭文件
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
    }

    //解密文件中内容,并将结果写入文件
    public static void DecryFile(String filename, String keyword) throws IOException {
        String result = readFileIntoString(filename);
        //将文件中读到的字符进行解密
        String plaintext = decrypt(result,keyword);
        //讲解密后的字符写入到文件中
        File file2 = new File("src\\ColumnPlaintext.txt");//我们在该类的位置创建一个新文件
        FileWriter fileWriter = null;//创建文件写入对象
        BufferedWriter f1 = null;//创建字符流写入对象
        try {
            //这里把文件写入对象和字符流写入对象分开写了
            fileWriter = new FileWriter("src\\ColumnPlaintext.txt");//创建一个名为cc.txt的文件
            f1 = new BufferedWriter(fileWriter);
            //通过循环遍历上面的String 数组中的元素
            for (int i = 0; i < plaintext.length(); i++) {
                f1.write(plaintext.charAt(i));//把String中的字符写入文件
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            try {
                f1.close();
                fileWriter.close();//关闭文件
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

    }

    //选择菜单
    public static int menu() {
        System.out.println("-------------------------------------------");
        System.out.println("-----------------options-------------------");
        System.out.println("*              1.加密/解密键盘输入的字符串     *");
        System.out.println("*              2.加密/解密文件               *");
        System.out.println("--------------------------------------------");
        Scanner sc = new Scanner(System.in);
        int result = sc.nextInt();
        return result;
    }

    //主函树进行测试
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int option = menu();
        if (option == 1) {
            System.out.println("请输入要进行操作的字符串:");
            String s = sc.nextLine();
            System.out.println("请输入密钥:");
            String key = sc.nextLine();
            System.out.println("请问是需要加密(1)还是解密(2):");
            int flag = sc.nextInt();
            if (flag == 1) {
                System.out.println("生成的密文是" +encrypt(s, key));
            } else {
                System.out.println("解密得到的明文是" + decrypt(s, key));
            }
        } else if (option == 2) {
            System.out.println("请输入要进行操作的文件名");
            String filename = sc.nextLine();
            System.out.println("请输入密钥:");
            String key = sc.nextLine();
            System.out.println("请问是需要加密(1)还是解密(2):");
            int flag1 = sc.nextInt();
            if (flag1 == 1) {
                EncryFile(filename, key);
            } else {
                DecryFile(filename, key);
            }
        } else {
            System.out.println("请选择<----------->");
        }
    }
}

