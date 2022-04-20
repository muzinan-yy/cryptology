package classical;

import java.io.*;
import java.util.Locale;
import java.util.Scanner;

public class VigenereCipher {
    static String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    //处理密钥 生成要进行加密操作的字符串
    public static String dealKey(String key, int len) {
        key = key.toUpperCase(Locale.ROOT);
        key = key.replaceAll("[^A-Z]", "");
        StringBuilder stringBuilder = new StringBuilder(key);
        String result = "";
        while (stringBuilder.length() < len) {
            stringBuilder.append(key);
        }
        result = stringBuilder.substring(0, len);
        return result;
    }

    //加密方法
    public static String Encryption(String keyword, String plaintext) {
        if (keyword == "") return "没有指定密钥";
        if (plaintext == "") return "没有加密的对象";
        plaintext = plaintext.toUpperCase(Locale.ROOT);
        plaintext = plaintext.replaceAll("[^A-Z]", "");
        System.out.println(plaintext.length());
        keyword = dealKey(keyword, plaintext.length());
        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            int index1 = alpha.indexOf(keyword.charAt(i));
            int index2 = alpha.indexOf(plaintext.charAt(i));
            int index = (index1 + index2) % 26;
            ciphertext.append(alpha.charAt(index));
        }
        return ciphertext.toString();
    }

    //解密方法
    public static String Decryption(String keyword, String ciphertext) {
        if (keyword == "") return "没有指定密钥";
        if (ciphertext == "") return "没有解密的对象";
        ciphertext = ciphertext.toUpperCase(Locale.ROOT);
        ciphertext = ciphertext.replaceAll("[^A-Z]", "");
        keyword = dealKey(keyword, ciphertext.length());
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i++) {
            int row = alpha.indexOf(keyword.charAt(i));
            int col = alpha.indexOf(ciphertext.charAt(i));
            int index;
            if (col >= row) {
                index = col - row;
            } else {
                index = col + 26 - row;
            }
            plaintext.append(alpha.charAt(index));
        }
        return plaintext.toString();
    }

    //文本文件的加解密
    //将文件内容读入字符
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

    //加密文件中内容,并将结果写入文件
    public static void EncryFile(String filename, String keyword) throws IOException {
        String plaintext = readFileIntoString(filename);
        String cipher = Encryption(keyword, plaintext);
        File file = new File("src\\vigenereCipher.txt");//我们在该类的位置创建一个新文件
        FileWriter fileWriter = null;//创建文件写入对象
        BufferedWriter f1 = null;//创建字符流写入对象
        try {
            //这里把文件写入对象和字符流写入对象分开写了
            fileWriter = new FileWriter("src\\vigenereCipher.txt");//创建一个名为cc.txt的文件
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
        String plaintext = Decryption(keyword, result);
        //讲解密后的字符写入到文件中
        File file2 = new File("src\\vigenerePlaintext.txt");//我们在该类的位置创建一个新文件
        FileWriter fileWriter = null;//创建文件写入对象
        BufferedWriter f1 = null;//创建字符流写入对象
        try {
            //这里把文件写入对象和字符流写入对象分开写了
            fileWriter = new FileWriter("src\\vigenerePlaintext.txt");//创建一个名为cc.txt的文件
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

    //菜单
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

    //主函数进行测试
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
                System.out.println("生成的密文是" + Encryption(key, s));
            } else {
                System.out.println("解密得到的明文是" + Decryption(key, s));
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
