package DES;

import java.io.*;
import java.util.Scanner;

public class DES {
    //初始置换IP(针对明文)
    static int IP[] = {
            24, 58, 50, 42, 34, 26, 18, 10, 2,
            25, 60, 52, 44, 36, 28, 20, 12, 4,
            26, 62, 54, 46, 38, 30, 22, 14, 6,
            27, 64, 56, 48, 40, 32, 24, 16, 8,
            28, 57, 49, 41, 33, 25, 17, 9, 1,
            29, 59, 51, 43, 35, 27, 19, 11, 3,
            30, 61, 53, 45, 37, 29, 21, 13, 5};
    //密钥置换，将64位的二进制数据置换为56位
    static int PC_1[] = {
            35, 57, 49, 41, 33, 25, 17, 9, 36,
            1, 58, 50, 42, 34, 26, 18, 37, 10,
            2, 59, 51, 43, 35, 27, 38, 19, 11,
            3, 60, 52, 44, 36, 39, 63, 55, 47,
            39, 31, 23, 15, 40, 7, 62, 54, 46,
            38, 30, 22, 41, 14, 6, 61, 53, 45,
            37, 29, 42, 21, 13, 5, 28, 20, 12, 4, 43};
    //密钥置换,将56位二进制数据置换为48位
    static int PC_2[] = {
            1, 8, 50, 42, 34, 26, 18,
            10, 2, 20, 51, 43, 35, 27,
            19, 11, 3, 16, 52, 44, 36,
            17, 55, 47, 39, 31, 23, 15,
            7, 19, 54, 46, 38, 30, 22,
            14, 6, 24, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12
    };
    //密钥置换,将32位二进制数据置换为48位
    static int EBox[] = {
            32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30
            , 31, 32, 1
    };

    //SBox将48位通过查表替代变成32位
    //所有的数都是不大于15的数
    static int SBox[][][] = {
            { //Sbox_1
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            { //Sbox_2
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            { //Sbox_3
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            { //Sbox_4
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            { //Sbox_5
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            { //Sbox_6
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            { //Sbox_7
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            { //Sbox_8
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }

    };

    static int PBox[] =

            {
                    16, 7, 20, 21, 29, 12, 28, 17,
                    1, 15, 23, 26, 5, 18, 31, 10,
                    2, 8, 24, 14, 32, 27, 3, 9,
                    19, 13, 30, 6, 22, 11, 4, 25
            };
    static int FT[] =

            {
                    40, 8, 48, 16, 56, 24, 64, 32,
                    39, 7, 47, 15, 55, 23, 63, 31,
                    38, 6, 46, 14, 54, 22, 62, 30,
                    37, 5, 45, 13, 53, 21, 61, 29,
                    36, 4, 44, 12, 52, 20, 60, 28,
                    35, 3, 43, 11, 51, 19, 59, 27,
                    34, 2, 42, 10, 50, 18, 58, 26,
                    33, 1, 41, 9, 49, 17, 57, 25
            };

    //获得64位的二进制密钥
    public static int[] keyToB(String key) {
        int key56[] = new int[64];
        int key64[] = new int[64];
        int h;
        int m = 0;
        for (int i = 6; i >= 0; i--) {
            h = key.charAt(i);
            for (int j = 0; j < 8; j++) {
                key56[56 - 1 - m] = h % 2;
                h /= 2;
                m++;
            }
        }
        System.out.println("------56kits-------");
        for (int i = 0; i < 56; i++) {
            System.out.print(key56[i]);
        }
        System.out.println();
        System.out.println("-------------------");
        int f = 0;
        int n = 0;
        int temp = 0;
        //64位,设置了奇偶检验位
        for (int i = 0; i < 56; i++) {
            key64[f] = key56[i];
            f++;
            if (key56[i] == 0) temp++;
            if (((n + 1) % 7 == 0) && (temp % 2 != 0)) {
                key64[f] = 1;
                f++;
                n++;
                continue;
            } else if (((n + 1) % 7 == 0) && (temp % 2 == 0)) {
                key64[f] = 0;
                f++;
                n++;
                continue;
            }
            n++;
        }
        System.out.println("--------64bits-------");
        for (int i = 0; i < 64; i++) {
            System.out.print(key64[i]);
        }
        System.out.println();
        System.out.println("---------------------");
        return key64;
    }

    //生成要使用的16个子密钥
    //对于密钥的处理,pc-1 pc-2,利用数组进行存储
    public static int[][] subKey(int[] key, int way) {
        int[][] subKey = new int[16][48];
        int[] temp0 = new int[56];
        int[] tempLeft = new int[28];
        int[] tempRight = new int[28];
        int[] combine = new int[56];
        //初始化置换PC-1
        for (int i = 0; i < 56; i++) {
            temp0[i] = key[(PC_1[i] - 1)];
            if (i < 28) tempLeft[i] = key[(PC_1[i] - 1)];
            else tempRight[i - 28] = key[(PC_1[i] - 1)];
        }
        //PC_1 over
        //进行输出检验
        System.out.println("------------PC_1-----------");
        for (int i = 0; i < 56; i++) System.out.print(temp0[i]);
        System.out.println();
        System.out.println("---------分为左右两个部分-----");
        //循环左移
        int i, j, k;
        int flag;
        int l;
        int r;
        for (i = 0; i < 16; i++) {
            //在第0,1,8,15轮的时候向左移动1位
            //其他轮的时候向左移动2位
            if (i == 0 || i == 1 || i == 8 || i == 15) {
                l = tempLeft[0];
                r = tempRight[0];
                for (j = 0; j < 27; j++) {
                    tempLeft[j] = tempLeft[j + 1];
                    tempRight[j] = tempRight[j + 1];
                }
                tempLeft[j] = l;
                tempRight[j] = r;
            } else {
                for (k = 0; k < 2; k++) {
                    l = tempLeft[0];
                    r = tempRight[0];
                    for (j = 0; j < 27; j++) {
                        tempLeft[j] = tempLeft[j + 1];
                        tempRight[j] = tempRight[j + 1];
                    }
                    tempLeft[j] = l;
                    tempRight[j] = r;
                }
            }
            //输出:L and R
            /*System.out.println("L<" + i + 1 + ">:");
            for (int i1 : tempLeft) System.out.print(i1);
            System.out.println();
            System.out.println("R<" + i + 1 + ">:");
            for (int i1 : tempRight) System.out.print(i1);
            System.out.println();*/

            //combine L and R
            for (flag = 0; flag < 56; flag++) {
                if (flag < 28) combine[flag] = tempLeft[flag];
                else combine[flag] = tempRight[flag - 28];
            }

            //way表示是加密还是解密 加密为0 解密为1
            //PC_2
            //置换
            if (way == 0) {
                for (flag = 0; flag < 48; flag++) {
                    subKey[i][flag] = combine[(PC_2[flag] - 1)];
                }
            } else {
                for (flag = 0; flag < 48; flag++) {
                    subKey[15 - i][flag] = combine[(PC_2[flag] - 1)];
                }
            }
        }

        //输出subKey
        System.out.println("-----------subKey---------");
        for (i = 0; i < 16; i++) {
            for (j = 0; j < 48; j++) {
                System.out.print(subKey[i][j]);
            }
            System.out.println();
        }
        System.out.println("------subKey over---------");
        return subKey;
    }

    //将明文转换为二进制块 64bit为一个block  way表示加/解密
    public static int[][] plainTextToB(String plaintext, int way) {
        int m = plaintext.length() / 8;
        int h = plaintext.length() % 8;
        if (h != 0) m++;
        int flag = m - 1;
        int[][] plain = new int[m][64];
        int i, t, j, n = 0;
        for (i = 0; i < plaintext.length(); i++) {
            t = plaintext.charAt(plaintext.length() - 1 - i);
            //System.out.println(t);
            //或许t>256
            for (j = 0; j < 8; j++) {
                if (n == 64) {
                    n = 0;
                    m--;
                }
                if (m - 1 != flag) plain[m - 1][64 - 1 - n] = t % 2;
                else {
                    plain[m - 1][h * 8 - 1 - n] = t % 2;
                    if (n == (8 * h - 1)) {
                        n = -1;
                        m--;
                    }
                }
                t /= 2;
                n++;
            }
        }

        //加密为0 解密为1
        //无论是加密还是解密生成的二进制的流总归是一样的
        if (way == 0) System.out.println("----------plain-------------");
        else System.out.println("--------------cipher------------");
        for (i = 0; i < flag + 1; i++) {
            for (j = 0; j < 64; j++) {
                System.out.print(plain[i][j]);
            }
            System.out.println();
        }
        System.out.println("---------------over---------------");
        return plain;
    }

    //明文初始化置换
    public static int[][] initP(int[][] plain) {
        int m = plain.length % 64;
        int[][] initPlain = new int[m][64];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 64; j++) {
                initPlain[i][j] = plain[i][(IP[i] - 1)];
            }
        }
        return initPlain;
    }

    //EBox
    public static int[] EBox(int[] tempRight) {
        int[] result = new int[48];
        int i;
        for (i = 0; i < 48; i++) {
            result[i] = tempRight[EBox[i] - 1];
        }
        return result;
    }

    //SBox
    public static int[] SBox(int[] tempRight, int[][] key, int flag) {//使用flag表示是第几轮的加密
        int[] temp = new int[48];
        System.out.println("这是第" + flag + "轮加密,密钥是:");
        for (int i = 0; i < 48; i++) {
            System.out.print(key[flag][i]);
        }
        //异或和分组操作
        int count = 0;
        int[][] temp2 = new int[8][6];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 6; j++) {
                temp2[i][j] = tempRight[count] ^ key[flag][count];
            }
        }
        int[] tempNum = new int[8];
        for (int i = 0; i < 8; i++) {
            int row = temp2[i][0] * 2 + temp2[i][5];
            int column = temp2[i][1] * 8 + temp2[i][2] * 4 + temp2[i][3] * 2 + temp2[i][4];
            tempNum[i] = SBox[i][row][column];
        }

        //二进制的转换
        int count2 = 0;
        int num = 0;
        int[] result = new int[32];
        for (int i = 0; i < 8; i++) {
            num = tempNum[7 - i];
            for (int j = 0; j < 4; j++) {
                result[32 - 1 - count2] = num % 2;
                num /= 2;
                count2++;
            }
        }

        //结果输出检验:
        System.out.println();
        System.out.println("第" + flag + "轮SBOX加密后的结果为:");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
        }
        System.out.println();
        return result;

    }

    //PBox
    public static int[] PBox(int[] tempRight) {
        int[] result = new int[32];
        for (int i = 0; i < 32; i++) {
            result[i] = tempRight[PBox[i] - 1];
        }
        return result;
    }

    //对明文实现16轮加密
    public static int[][] enCry(String Plaintext, String keyword) {
        int[][] temp1 = plainTextToB(Plaintext, 0);
        int[][] temp = initP(temp1);//初始化置换
        int len = temp.length % 64;
        int[] left = new int[32];
        int[] right = new int[32];
        int[][] key = subKey(keyToB(keyword), 0);
        for (int h = 0; h < len; h++) {
            //进行分割
            for (int i = 32; i < 64; i++) {
                right[i - 32] = temp[h][i];
            }
            for (int i = 0; i < 16; i++) {
                left = right;
                right = PBox(SBox(EBox(right), key, i));//申明是加密
            }
            int[] temp2 = finalTP(left, right);
            for (int i = 0; i < temp2.length; i++) {
                temp1[h][i] = temp2[i];
            }
        }
        //输出结果
        System.out.println("生成的密文为:");
        for (int[] arr : temp1) {
            for (int i : arr) {
                System.out.print(i);
            }
            System.out.println();
        }
        System.out.println();
        return temp1;
    }

    public static int[][] deCryString(String Ciphertext,String keyword){
        return deCry(plainTextToB(Ciphertext,1),keyword);
    }
    //对密文实现16轮的解密 就是对way的数值进行改变
    public static int[][] deCry(int[][] temp1, String keyword) {
        int[][] temp = initP(temp1);//初始化置换
        int len = temp.length % 64;
        int[] left = new int[32];
        int[] right = new int[32];
        int[][] key = subKey(keyToB(keyword), 1);
        for (int h = 0; h < len; h++) {
            //进行分割
            for (int i = 32; i < 64; i++) {
                right[i - 32] = temp[h][i];
            }
            //轮函数
            for (int i = 0; i < 16; i++) {
                left = right;
                right = PBox(SBox(EBox(right), key, i));//申明是加密
            }
            int[] temp2 = finalTP(left, right);
            for (int i = 0; i < temp2.length; i++) {
                temp1[h][i] = temp2[i];
            }
        }
        System.out.println("生成的明文为:");
        //输出结果
        for (int[] arr : temp1) {
            for (int i : arr) {
                System.out.print(i);
            }
            System.out.println();
        }
        System.out.println();
        return temp1;
    }

    //FinalTp
    public static int[] finalTP(int[] left, int[] right) {
        int[] temp = new int[64];
        for (int j = 0; j < 64; j++) {
            if (j < 32) {
                temp[j] = left[j];
            } else {
                temp[j] = right[j - 32];
            }
        }

        int[] result = new int[64];
        for (int i = 0; i < 64; i++) {
            result[i] = temp[FT[i] - 1];
        }
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

    //加密文件
    public static void EncryFile(String filename, String keyword) throws IOException {
        String plaintext = readFileIntoString(filename);
        int[][] temp = enCry(plaintext, keyword);
        File file = new File("src\\DESCipher.txt");//我们在该类的位置创建一个新文件
        FileWriter fileWriter = null;//创建文件写入对象
        BufferedWriter f1 = null;//创建字符流写入对象
        try {
            //这里把文件写入对象和字符流写入对象分开写了
            //文件不是追加模式,每次都会覆盖掉之前的
            fileWriter = new FileWriter("src\\DESCipher.txt");//创建一个名为cc.txt的文件
            f1 = new BufferedWriter(fileWriter);
            //通过循环遍历上面的String 数组中的元素
            for (int[] i : temp) {
                for (int j : i) {
                    System.out.println(j);
                    f1.write(String.valueOf(j));
                }
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

    //解密二进制文件
    public static int[][] deCryB(String ciphertext, String keyword) {
        int h=ciphertext.length()%64;
        int m=ciphertext.length()/64;
        if(h!=0) m++;
        int[][] temp1=new int[m][64];
        int l=0;
        for(int i=0;i<m;i++){
            for(int j=0;j<64;j++){
                if(l<ciphertext.length()) {
                    temp1[i][j] = Integer.parseInt(String.valueOf(ciphertext.charAt(l)));
                }else{
                    temp1[i][j]=0;
                }
                l++;
            }
        }


        //输出从文件中读出的二进制数据
        for( int i=0;i<m;i++){
            for(int j=0;j<64;j++){
                System.out.print(temp1[i][j]);
            }
        }
        System.out.println();
      return deCry(temp1,keyword);
    }

    //解密文件中内容,并将结果写入文件
    public static void DecryFile(String filename, String keyword) throws IOException {
        String result = readFileIntoString(filename);
        //将文件中读到的字符进行解密
        int[][] ciphertext = deCryB(result, keyword);
        //讲解密后的字符写入到文件中
        File file2 = new File("src\\DESPlaintext.txt");//我们在该类的位置创建一个新文件
        FileWriter fileWriter = null;//创建文件写入对象
        BufferedWriter f1 = null;//创建字符流写入对象
        try {
            //这里把文件写入对象和字符流写入对象分开写了
            fileWriter = new FileWriter("src\\DESPlaintext.txt");//创建一个名为cc.txt的文件
            f1 = new BufferedWriter(fileWriter);
            //通过循环遍历上面的String 数组中的元素
            for (int[] i : ciphertext) {
                for (int j : i) {
                    f1.write(String.valueOf(j));
                }
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
        System.out.println("-----------------------");
        System.out.println("-请选择你要操作的对象----");
        System.out.println("-      1.文件         -");
        System.out.println("-      2.键盘输入      -");
        System.out.println("----------------------");
        Scanner sc = new Scanner(System.in);
        int option = sc.nextInt();
        return option;
    }

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        while (true) {
            int option = menu();
            System.out.println("------------------");
            if (option == 2) {
                System.out.println("请输入要进行操作的字符串:");
                String s = sc.nextLine();
                System.out.println("请输入密钥:");
                String key = sc.nextLine();
                System.out.println("请问是需要加密(1)还是解密(2):");
                int flag = sc.nextInt();
                if (flag == 1) {
                    enCry(s, key);
                } else {
                    deCryString(s, key);
                }
                break;
            } else if (option == 1) {
                System.out.println("请输入要进行操作的文件名:");
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
                break;
            } else {
                System.out.println("------请选择--------");
            }
        }
    }
}