// JavaScript Document
/*****************************************************  
 *  ��������loginCheck()
 *  ��  �ã���¼��������֤
 *	��  ������ 
 *  ����ֵ��True: ��֤ͨ��
 *          False: ��֤û��ͨ��
 *****************************************************
*/
 function loginCheck()
 {
	var loginCheck = false;
	var userName = document.loginForm.userName.value;
	if (userName == "") {
	alert("����������û���!");
	document.loginForm.userName.focus();
		return false;
	}
	
	
	
	var userPwd = document.loginForm.userPwd.value;
	if (userPwd == "") {
	alert("�������������!");
	document.loginForm.userPwd.focus();
		return false;
	}
	
	var code = document.loginForm.code.value;
	if (code == "" || code.length != 4) {
	alert("��������ȷ����֤��!");
	document.loginForm.code.focus();
		return false;
	}
  return true;
  }  
  
/*****************************************************  
 *  ��������anotherImg()
 *  ��  �ã�ˢ����֤��
 *	��  ������ 
 *****************************************************
*/ 
function anotherImg()
{
	var obj = document.getElementById("code");
	obj.src="../util/rand.jsp?rnd="+Math.random();
}
