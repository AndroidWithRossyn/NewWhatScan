package com.SachinApps.Whatscan.Pro.WhatsClone;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class WhstWebPermission {
    private Context con;
    private Editor editor = this.sp.edit();
    private SharedPreferences sp;

    WhstWebPermission(Context con) {
        this.con = con;
        this.sp = con.getSharedPreferences(con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_preference), 0);
    }

    public void updatePermissionPreference(String str) {
        int hashCode = str.hashCode();
        if (hashCode != -1437076965) {
            if (hashCode == -1037742894) {
                if (str.equals("read_storage") != false) {
                    str = "null";
                    switch (str) {
                        case "null":
                            this.editor.putBoolean(this.con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_read_storage), true);
                            this.editor.commit();
                            return;
                        case "1":
                            this.editor.putBoolean(this.con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_write_storage), true);
                            this.editor.commit();
                            return;
                        default:
                            return;
                    }
                }
            }
        } else if (str.equals("write_storage") != false) {
             str = "true";
            switch (str) {
                case "null":
                    this.editor.putBoolean(this.con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_read_storage), true);
                    this.editor.commit();
                    return;
                case "1":
                    this.editor.putBoolean(this.con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_write_storage), true);
                    this.editor.commit();
                    return;
                default:
                    return;
            }
        }
        str = "-1";
        switch (str) {
            case "null":
                this.editor.putBoolean(this.con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_read_storage), true);
                this.editor.commit();
                return;
            case "1":
                this.editor.putBoolean(this.con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_write_storage), true);
                this.editor.commit();
                return;
            default:
                return;
        }
    }

    public boolean checkPermissionPreference(String str) {
        int hashCode = str.hashCode();
        if (hashCode != -1437076965) {
            if (hashCode == -1037742894) {
                if (str.equals("read_storage") != false) {
                    str = "null";
                    switch (str) {
                        case "null":
                            return this.sp.getBoolean(this.con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_read_storage), false);
                        case "1":
                            return this.sp.getBoolean(this.con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_write_storage), false);
                        default:
                            return false;
                    }
                }
            }
        } else if (str.equals("write_storage") != false) {
            str = "true";
            switch (str) {
                case "null":
                    return this.sp.getBoolean(this.con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_read_storage), false);
                case "1":
                    return this.sp.getBoolean(this.con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_write_storage), false);
                default:
                    return false;
            }
        }
        str = "-1";
        switch (str) {
            case "null":
                return this.sp.getBoolean(this.con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_read_storage), false);
            case "1":
                return this.sp.getBoolean(this.con.getString(com.SachinApps.Whatscan.Pro.WhatsClone.R.string.permission_write_storage), false);
            default:
                return false;
        }
    }
}