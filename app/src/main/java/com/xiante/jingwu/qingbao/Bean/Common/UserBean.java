package com.xiante.jingwu.qingbao.Bean.Common;

import java.io.Serializable;


public class UserBean implements Serializable {
   private String strAddress;
   private String strName;
   private String strSex;
   private String strProfession;
   private String strRegionGuid;
   private Boolean isComplement;
   private Boolean isLogin;
   private String  intAge;
   private String isEnable;
   private String strAccount;

   @Override
   public String toString() {
      return "UserBean{" +
              "strAddress='" + strAddress + '\'' +
              ", strName='" + strName + '\'' +
              ", strSex='" + strSex + '\'' +
              ", strProfession='" + strProfession + '\'' +
              ", strRegionGuid='" + strRegionGuid + '\'' +
              ", isComplement=" + isComplement +
              ", isLogin=" + isLogin +
              ", intAge='" + intAge + '\'' +
              ", isEnable=" + isEnable +
              ", strAccount='" + strAccount + '\'' +
              '}';
   }

   public String getStrAddress() {
      return strAddress;
   }

   public void setStrAddress(String strAddress) {
      this.strAddress = strAddress;
   }

   public String getStrName() {
      return strName;
   }

   public void setStrName(String strName) {
      this.strName = strName;
   }

   public String getStrSex() {
      return strSex;
   }

   public void setStrSex(String strSex) {
      this.strSex = strSex;
   }

   public String getStrProfession() {
      return strProfession;
   }

   public void setStrProfession(String strProfession) {
      this.strProfession = strProfession;
   }

   public String getStrRegionGuid() {
      return strRegionGuid;
   }

   public void setStrRegionGuid(String strRegionGuid) {
      this.strRegionGuid = strRegionGuid;
   }

   public Boolean getComplement() {
      return isComplement;
   }

   public void setComplement(Boolean complement) {
      isComplement = complement;
   }

   public Boolean getLogin() {
      return isLogin;
   }

   public void setLogin(Boolean login) {
      isLogin = login;
   }

   public String getIntAge() {
      return intAge;
   }

   public void setIntAge(String intAge) {
      this.intAge = intAge;
   }

   public String  getEnable() {
      return isEnable;
   }

   public void setEnable(String enable) {
      isEnable = enable;
   }

   public String getStrAccount() {
      return strAccount;
   }

   public void setStrAccount(String strAccount) {
      this.strAccount = strAccount;
   }
}
