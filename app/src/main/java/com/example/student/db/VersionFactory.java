package com.example.student.db;

/**
 * @author somelou
 * @description
 * @date 2019-04-22
 */
public class VersionFactory {

    public static UpgradeDB getUpgrade(int i) {
        UpgradeDB upgrade = null;
        switch (i) {
            case 4:
                upgrade = new VersionSecond();
                break;
            case 5:
                upgrade = new VersionThird();
                break;
        }
        return upgrade;
    }

}
