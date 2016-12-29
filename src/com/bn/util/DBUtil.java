package com.bn.util;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.*;
/*
 * 数据库只返回3种信息：返回的数据类新为 String或List
 * 1.ok    	update/delete成功
 * 2.list 	select 结果
 * 3.null 	出现异常，数据库操作失败！
 * 
 * 
 */
public class DBUtil {

	// 从连接池中获得数据库连接
	private static Connection getConnection() {
		Connection con = null;
		try {
			Context ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/XWGLXTServer");
			con = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	// 登录
	private static Object LOGIN_LOCK = new Object();

	public static String login(String ygdl, String ygmm) {
		synchronized (LOGIN_LOCK) {
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			try {
				st = con.createStatement();
				String str="select glymm, jsid, glyid, glyxm from gly where glydl = '"
					+ ygdl + "'";
				rs = st.executeQuery(str);
				boolean isgly=rs.next();
				String glymm;
				String glyjsid;
				String glyid;
				String glyxm;
				if(isgly)
				{
					glymm = rs.getString(1);
					glyjsid = rs.getString(2);
					glyid = rs.getString(3);
					glyxm = rs.getString(4);
					if (glymm.equals(ygmm)) {
						String str2 = "select qxid from qxfp where jsid=" + glyjsid;
						rs = st.executeQuery(str2);
						String jsqxid = "";
						while (rs.next()) {
							jsqxid += rs.getString(1) + ",";
						}
						jsqxid = glyid + "<->" + jsqxid + "<->" + glyxm;
						return jsqxid;
					} else {
						return "fail";
					}
				}else
				{
					String str1 = "select ygmm, jsid, ygid, ygxm from yg where ygdl = '"
						+ ygdl + "'";
				rs = st.executeQuery(str1);
				rs.next();
				String mm;
				String jsid;
				String ygid;
				String ygxm;
				try {
					mm = rs.getString(1);
					jsid = rs.getString(2);
					ygid = rs.getString(3);
					ygxm = rs.getString(4);
				} catch (Exception e) {
					return "fail";
				}
				if (mm.equals(ygmm)) {
					String str2 = "select qxid from qxfp where jsid=" + jsid;
					rs = st.executeQuery(str2);
					String jsqxid = "";
					while (rs.next()) {
						jsqxid += rs.getString(1) + ",";
					}
					jsqxid = ygid + "<->" + jsqxid + "<->" + ygxm;
					return jsqxid;
				} else {
					return "fail";
				}	
			}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 获得基本权限(list)
	public static List<String[]> getJBQX(String jsid) {
		Connection con = getConnection();
		List<String[]> result = new ArrayList<String[]>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			String ts = "select qxid,qxmc from qx where qxid in (select qxid from qxfp where jsid="
					+ jsid + ")";
			rs = st.executeQuery(ts);
			while (rs.next()) {
				String t[] = new String[2];
				t[0] = rs.getString(1);
				t[1] = rs.getString(2);
				result.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 修改角色名称(ok)
	private static Object xg_JSMC = new Object();

	public static String xgJSMC(int jsid, String jsmc) {
		synchronized (xg_JSMC) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String sql = "update js set jsmc='" + jsmc + "' where jsid="
						+ jsid;
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 获得角色信息(list)
	public static List<String[]> getJS() {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select jsid,jsmc from js";
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[2];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 添加角色(ok)
	private static Object ADDJS_LOCK = new Object();

	public static String addJS() {// 添加一个角色 ID 为表中最大ID+1 名称为"在次处填入角色名称" 所有权限为"";
		synchronized (ADDJS_LOCK) {
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			try {
				st = con.createStatement();
				String str = "select max(jsid+1) from js";
				rs = st.executeQuery(str);
				rs.next();
				int max = rs.getInt(1);
				String sql = "insert into js values(" + max + ",'请在次处填入角色名称')";
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 删除角色(ok/fail角色下有员工)
	public static String scJS(String jsid) {

		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			st = con.createStatement();
			String task = " select ygid from yg where jsid = " + jsid;
			rs = st.executeQuery(task);
			while (rs.next()) {
				String t = rs.getString(1);
				list.add(t);
			}
			if (list.size() > 0)
				return "fail";
			else {
				String del = "delete from qxfp where jsid = " + jsid;
				st.executeUpdate(del);
				del = " delete from js where jsid = " + jsid;
				st.executeUpdate(del);
				return "ok";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 获得不具有的权限(list)
	public static List<String[]> getBJYDQX(String jsid) {

		Connection con = getConnection();
		List<String[]> allqx = new ArrayList<String[]>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			String str1 = "select qxid,qxmc from qx where qxid not in (select qxid from qxfp where jsid="
					+ jsid + ")";
			rs = st.executeQuery(str1);
			while (rs.next()) {
				String t[] = new String[2];
				t[0] = rs.getString(1);
				t[1] = rs.getString(2);
				allqx.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return allqx;
	}

	// 给指定角色 添加 名称为qxmc 的权限ID(ok)
	private static Object addJSQXByQXMC = new Object();

	public static String addJSQXByQXMC(int jsid, String qxmc) {
		synchronized (addJSQXByQXMC) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String sql = "insert into qxfp values(" + jsid
						+ ",(select qxid from qx where qxmc='" + qxmc + "'))";
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 删除角色具有的某个权限(ok)
	public static String scQX(int jsid, int qxid) {
		Connection con = getConnection();
		Statement st = null;
		try {
			st = con.createStatement();
			String sql = "delete from qxfp where qxid=" + qxid + " and jsid="
					+ jsid;
			st.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "ok";
	}

	// 获得角色信息(list)
	public static List<String[]> getYG(int lzyf) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = new String();
			if (lzyf == 2) {
				task = "select ygid,ygdl,ygmm,ygxm,ygxb,lxfs,bmid,jsid,lzyf from yg";
			} else {
				task = "select ygid, ygdl, ygmm, ygxm, ygxb, lxfs, bmid, jsid,lzyf from yg where lzyf ="
						+ lzyf;
			}
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[10];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				str[3] = rs.getString(4);
				str[4] = rs.getString(5);
				str[5] = rs.getString(6);
				str[6] = rs.getString(7);
				str[7] = rs.getString(8);
				str[8] = rs.getString(9);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 更新员工信息
	private static Object UPDATE_YGXX_LOCK = new Object();

	public static String updateYGXX(String[] s) {
		synchronized (UPDATE_YGXX_LOCK) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String sql = "update yg set bmid = " + s[1] + " ,jsid = "
						+ s[2] + ",lzyf= " + s[3] + " where ygid= " + s[0];
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 添加员工
	private static Object YGID_LOCK = new Object();

	public static String addYG(String dlzh, String dlmm, String ygxm,
			String lxfs, String xb, String bmid, String jsid) {// 员工部门ID 员工姓名
																// 员工性别 员工密码
																// 员工级别ID
		synchronized (YGID_LOCK) {
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			try {
				st = con.createStatement();
				String getMaxId = "select max(ygid) from yg";
				rs = st.executeQuery(getMaxId);
				rs.next();
				int id = rs.getInt(1) + 1;
				int lzyf = 0;
				String insert = "insert into yg values(" + id + ",'" + dlzh
						+ "','" + ygxm + "','" + xb + "','" + dlmm + "','"
						+ lxfs + "'," + bmid + "," + jsid + "," + lzyf + ")";
				st.executeUpdate(insert);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 获得部门
	public static List<String[]> getBM() {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select bmid,fbmid,bmmc,bmms from bm";
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[4];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				str[3] = rs.getString(4);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 重组部门
	private static Object CZ_LOCK = new Object();

	public static String czBM(int id, int pid) {
		synchronized (CZ_LOCK) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String sql = "update bm set fbmid=" + pid + " where bmid=" + id;
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 获得最大部门ID
	public static String getMaxBMID() {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String result = null;
		try {
			st = con.createStatement();
			String sql = "select max(bmid) from bm";
			rs = st.executeQuery(sql);
			rs.next();
			result = rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 获得子节点
	private static void getSonNode(int id, List<Integer> bmIdList,
			Connection con) {
		bmIdList.add(id);
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			String sqlL = "select bmid from bm where fbmid=" + id;
			rs = st.executeQuery(sqlL);
			while (rs.next()) {
				int tempBmId = rs.getInt(1);
				getSonNode(tempBmId, bmIdList, con);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 获得指定部门ID下员工所有信息
	public static List<String[]> getYGByBMID(int id) {
		// 本部门及其下属部门的ID列表
		List<Integer> bmIdList = new ArrayList<Integer>();
		// 获取数据库连接
		Connection con = getConnection();
		// 调用递归方法找出所有子部门(也包括自身id)
		getSonNode(id, bmIdList, con);
		// 遍历bmIdList里面的所有部门ID生成SQL语句检索出所有的员工
		List<String[]> result = new ArrayList<String[]>();
		Statement st = null;
		ResultSet rs = null;
		try {
			// .....生成含有or的SQL语句检索所有符合条件的员工的ygid,ygxm
			StringBuilder sqlsb = new StringBuilder();
			sqlsb.append("select ygid,ygxm from yg where ");
			for (int i = 0; i < bmIdList.size(); i++) {
				sqlsb.append("bmid=" + bmIdList.get(i));
				if (i != bmIdList.size() - 1) {
					sqlsb.append(" or ");
				}
			}
			String sql = sqlsb.toString();
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				String[] str = new String[2];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				result.add(str);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 删除部门
	private static Object DELETEBM_LOCK = new Object();

	public static String deleteBM(int id) {

		synchronized (DELETEBM_LOCK) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String sql = "delete from bm where bmid=" + id;
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 添加部门
	private static Object BMID_LOCK = new Object();

	public static String addBM(int pid, String mc, String ms) {
		synchronized (BMID_LOCK) {
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			try {
				st = con.createStatement();
				String str = "select max(bmid) from bm";
				rs = st.executeQuery(str);
				rs.next();
				int max = rs.getInt(1) + 1;
				String sql = "insert into bm values(" + max + "," + pid + ",'"
						+ mc + "','" + ms + "');";
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}

	}

	// 更新部门
	private static Object UPDATE_BM_LOCK = new Object();

	public static String updateBM(int id, String mc, String ms) {
		synchronized (UPDATE_BM_LOCK) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String sql = "update bm set bmmc='" + mc + "',bmms='" + ms
						+ "' where bmid=" + id;
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 个人信息修改
	private static Object UPDATA_YG_LOCK = new Object();

	public static String updata_yg(String ygid, String ysmm, String xmm,
			String ygxm, String ygxb, String lxfs) {
		synchronized (UPDATA_YG_LOCK) {
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			try {
				st = con.createStatement();
				String str1 = "select ygmm from yg where ygid = " + ygid;
				rs = st.executeQuery(str1);
				rs.next();
				String mm;
				try {
					mm = rs.getString(1);
				} catch (Exception e) {
					return null;
				}
				if (mm.equals(ysmm)) {
					String updataTask = "update yg set ygmm ='" + xmm
							+ "',ygxm='" + ygxm + "',ygxb='" + ygxb
							+ "',lxfs ='" + lxfs + "' where ygid =" + ygid;
					st.executeUpdate(updataTask);
					return "ok";
				} else {
					return "fail";
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 添加新闻
	private static Object ADD_NEW = new Object();

	public static String[] addNEW(String xwbt, String xwgs, String xwly,
			String fbsj, String xwnr, String ygid, String ztid, String bsid) {
		int max = 0;
		String[] result = new String[2];
		synchronized (ADD_NEW) {
			String fbztid = "0";
			String lmid = "0";
			String sxid = null;
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			try {
				st = con.createStatement();
				String str = "select max(xwid) from xw";
				rs = st.executeQuery(str);
				rs.next();
				max = rs.getInt(1) + 1;
				sxid=max+"";
				String sql = "insert into xw values(" + max + " , '" + xwbt
						+ "','" + xwgs + "','" + xwly + "','" + fbsj + "','"
						+ xwnr + "'," + bsid + "," + lmid + "," + ygid + ","
						+ ztid + "," + fbztid + "," + sxid + " )";
				st.executeUpdate(sql);
				if (ztid.equals("1")) {
					String task = "select max(shid) from shjl";
					rs = st.executeQuery(task);
					rs.next();
					int maxSH = rs.getInt(1) + 1;
					String task1 = "insert into shjl values(" + maxSH + ", "
							+ max + " ," + ygid + ", '" + fbsj + "'," + null
							+ ", " + null + ", " + null + "," + ztid + " )";
					st.executeUpdate(task1);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			result[0] = "ok";
			result[1] = "" + max;
			return result;
		}

	}

	// 获得新闻(list)
	public static List<String[]> getNEW(String ygid) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select xw.xwid, xw.xwbt,  xw.xwly, xw.fbsj, xwzt.ztmc, fbzt.fbztmc from xw, xwzt, fbzt"
					+ " where xw.ztid=xwzt.ztid and xw.fbztid= fbzt.fbztid and ygid = "+ygid;
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[6];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				str[3] = rs.getString(4);
				str[4] = rs.getString(5);
				str[5] = rs.getString(6);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 获得指定新闻如果有审核记录，得到审核意见，如果没有审核记录，只得到新闻信息
	public static List<String[]> getNewById(int xwid) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select xw.xwbt, xw.xwgs, xw.xwly, xw.fbsj, xw.xwnr, xw.ztid ,xw.bsid from xw where xw.xwid="
					+ xwid;
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[7];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				str[3] = rs.getString(4);
				str[4] = rs.getString(5);
				str[5] = rs.getString(6);
				str[6] = rs.getString(7);
				list.add(str);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 删除新闻(ok)
	private static Object del_New = new Object();

	public static String delNEW(int xwid) {
		synchronized (del_New) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String task = "delete from shjl where xwid=" + xwid;
				st.executeUpdate(task);
				String delPics="select tplj from tp where xwid ="+xwid;
				ResultSet res=st.executeQuery(delPics);
				while(res.next())
				{
					FileUtiles.DeleteFolder(com.bn.Constant.picPath+res.getString(1));
				}
				String del="delete from tp where xwid=" + xwid;
				st.executeUpdate(del);
				String sql = "delete from xw where xwid=" + xwid;
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 更新新闻
	private static Object update_New = new Object();
	//xwbt,xwgs,xwly,fbsj,xwnr,ygid,ztid,bsid,pic1MS,pic2MS,picTitelPath,pic1Path,pic2Path,
	public static String[] updateNEW(String xwid, String xwbt, String xwgs,String xwly, String fbsj, String xwnr,String ygid, String ztid,String bsid) 
	{
		synchronized (update_New) {
			String[] result=new String[2];
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			try {
				st = con.createStatement();
				String sql = "update xw set xwbt ='" + xwbt + "',xwgs='" + xwgs
						+ "',xwly='" + xwly + "',fbsj ='" + fbsj + "',xwnr='"
						+ xwnr + "',ygid = "+ygid+", ztid = "+ztid+", bsid=" + bsid + "  where xwid =" + xwid;
				st.executeUpdate(sql);
				if(ztid.equals("1"))
				{
					String task = "select max(shid) from shjl";
					rs = st.executeQuery(task);
					rs.next();
					int maxSH = rs.getInt(1) + 1;
					String task1 = "insert into shjl values(" + maxSH + ", " + xwid
							+ " ," + ygid + ", '" + fbsj + "'," + null + ", "
							+ null + ", " + null + "," + ztid + " )";
					st.executeUpdate(task1);	
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			result[0]="ok";
			result[1]=xwid;
			return result;
		}
	}
	
/*	// 添加新闻
	private static Object ADD_NEW = new Object();

	public static String[] addNEW(String xwbt, String xwgs, String xwly,
			String fbsj, String xwnr, String ygid, String ztid, String bsid) {
		int max = 0;
		String[] result = new String[2];
		synchronized (ADD_NEW) {
			String fbztid = "0";
			String lmid = "0";
			String sxid = null;
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			try {
				st = con.createStatement();
				String str = "select max(xwid) from xw";
				rs = st.executeQuery(str);
				rs.next();
				max = rs.getInt(1) + 1;
				sxid=max+"";
				String sql = "insert into xw values(" + max + " , '" + xwbt
						+ "','" + xwgs + "','" + xwly + "','" + fbsj + "','"
						+ xwnr + "'," + bsid + "," + lmid + "," + ygid + ","
						+ ztid + "," + fbztid + "," + sxid + " )";
				st.executeUpdate(sql);
				if (ztid.equals("1")) {
					String task = "select max(shid) from shjl";
					rs = st.executeQuery(task);
					rs.next();
					int maxSH = rs.getInt(1) + 1;
					String task1 = "insert into shjl values(" + maxSH + ", "
							+ max + " ," + ygid + ", '" + fbsj + "'," + null
							+ ", " + null + ", " + null + "," + ztid + " )";
					st.executeUpdate(task1);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			result[0] = "ok";
			result[1] = "" + max;
			return result;
		}

	}*/

	// 获得审核记录(list)
	public static List<String[]> getSHJL() {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select shjl.shid, shjl.xwid, xw.xwbt, yg.ygxm, shjl.tjsj, shjl.shrmc, shjl.shsj,xwzt.ztmc "
					+ "from shjl, yg, xw, xwzt where shjl.xwid=xw.xwid and shjl.ztid=xwzt.ztid and shjl.tjr=yg.ygid and shjl.ztid > 0 order by shjl.shid asc";
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[8];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				str[3] = rs.getString(4);
				str[4] = rs.getString(5);
				str[5] = rs.getString(6);
				str[6] = rs.getString(7);
				str[7] = rs.getString(8);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 获得过滤之后的审核记录(list)
	public static List<String[]> getSHJLFilter(String ztid, String rq,String xwid) {

		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select shjl.shid, shjl.xwid, xw.xwbt, yg.ygxm, shjl.tjsj, shjl.shrmc, shjl.shsj,xwzt.ztmc "
					+ "from shjl, yg, xw, xwzt where shjl.xwid=xw.xwid and xw.ztid=xwzt.ztid and shjl.tjr=yg.ygid ";
			if (!ztid.equals("no")) {
				String ztids = ztid.substring(0, ztid.length() - 1);
				task = task + "and (xw.ztid in (" + ztids + "))";
			}
			if (!rq.equals("no")) {
				String[] rqs = rq.split(",");
				task = task + "and (shjl.tjsj between '" + rqs[0] + "' and '"
						+ rqs[1] + "')";
			}
			if (!xwid.equals("no")) {
				task = task + "and shjl.xwid = " +xwid;
			}
			task = task + " order by shjl.shid asc";
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[8];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				str[3] = rs.getString(4);
				str[4] = rs.getString(5);
				str[5] = rs.getString(6);
				str[6] = rs.getString(7);
				str[7] = rs.getString(8);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 获得过滤之后的审核记录(list)
	public static List<String[]> getGRXWFilter(String ztid, String rq,String fbzt,String ygid) {

		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {

			st = con.createStatement();
			String task = "select xw.xwid, xw.xwbt, xw.xwly, xw.fbsj, xwzt.ztmc, fbzt.fbztmc  from xw, xwzt, fbzt where xw.ztid=xwzt.ztid and xw.fbztid=fbzt.fbztid and xw.ygid = "+ygid+" ";
			if (!ztid.equals("no")) {
				String ztids = ztid.substring(0, ztid.length() - 1);
				task = task + "and (xw.ztid in (" + ztids + "))";
			}
			if (!fbzt.equals("no")) {
				String fbztids = fbzt.substring(0, fbzt.length() - 1);
				task = task + "and (xw.fbztid in (" + fbztids + "))";
			}
			if (!rq.equals("no")) {
				String[] rqs = rq.split(",");
				task = task + "and (xw.fbsj between '" + rqs[0] + "' and '"
						+ rqs[1] + "')";
			}
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[6];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				str[3] = rs.getString(4);
				str[4] = rs.getString(5);
				str[5] = rs.getString(6);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 获得栏目(list)
	public static List<String[]> getLM() {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select sxid, lmid, lmmc from lm where lmid > 0 order by sxid asc";
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[3];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 修改栏目名称(ok)
	private static Object xg_LMMC = new Object();

	public static String xgLMMC(int lmid, String lmmc) {
		synchronized (xg_LMMC) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String sql = "update lm set lmmc='" + lmmc + "' where lmid="
						+ lmid;
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 获得栏目所含新闻(list)
	public static List<String[]> getLMXW(String lmid) {
		Connection con = getConnection();
		List<String[]> result = new ArrayList<String[]>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			String ts = "select sxid, xwid, xwbt from xw where lmid = " + lmid
					+ " and ztid =3 and fbztid =1 order by sxid asc";
			rs = st.executeQuery(ts);
			while (rs.next()) {
				String t[] = new String[3];
				t[0] = rs.getString(1);
				t[1] = rs.getString(2);
				t[2] = rs.getString(3);
				result.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	
	// 获得栏目所含新闻(list)
	public static List<String[]> getLMFBXW(String lmid) {
		Connection con = getConnection();
		List<String[]> result = new ArrayList<String[]>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			String ts = "select sxid, xwid, xwbt, xwgs, fbsj, ygxm from xw , yg  where xw.ygid = yg.ygid and lmid = " + lmid
					+ " and ztid =3 and fbztid =1 order by sxid asc";
			rs = st.executeQuery(ts);
			while (rs.next()) {
				String t[] = new String[6];
				t[0] = rs.getString(1);
				t[1] = rs.getString(2);
				t[2] = rs.getString(3);
				t[3] = rs.getString(4);
				t[4] = rs.getString(5);
				t[5] = rs.getString(6);
				result.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	
	// 获得未发布新闻(list)
	public static List<String[]> getXWDFB() {
		Connection con = getConnection();
		List<String[]> result = new ArrayList<String[]>();
		Statement st = null;
		ResultSet rs = null;
		try {
			st = con.createStatement();
			String ts = "select sxid, xwid,xwbt from xw where lmid =0 and ztid = 3 and fbztid = 0 order by sxid asc";
			rs = st.executeQuery(ts);
			while (rs.next()) {
				String t[] = new String[3];
				t[0] = rs.getString(1);
				t[1] = rs.getString(2);
				t[2] = rs.getString(3);
				result.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// 获得指定新闻(list)
	public static List<String[]> getSHById(int shid) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select xw.xwbt, xw.xwgs, xw.xwly, shjl.tjsj, xw.xwnr, xw.bsid ,shjl.shyj, shjl.xwid from xw, shjl where xw.xwid =shjl.xwid "
					+ " and shjl.shid = " + shid;
			// "and xw.xwid=(select xwid from shjl where shid ="+shid+")";
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[8];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				str[3] = rs.getString(4);
				str[4] = rs.getString(5);
				str[5] = rs.getString(6);
				if (rs.getString(7) == null) {
					str[6] = " ";
				} else {
					str[6] = rs.getString(7);
				}
				str[7] = rs.getString(8);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 更新审核记录
	public static String updateSHJL(String shid, String ztid, String shrxm,
			String shsj, String shyj) {
		Connection con = getConnection();
		Statement st = null;
		try {
			st = con.createStatement();
			String sql = "update xw set ztid= " + ztid
					+ " where xwid = (select xwid from shjl where shid = "
					+ shid + " )";
			st.executeUpdate(sql);
			String task = "update shjl set shrmc= '" + shrxm + "', shsj = '"
					+ shsj + "', shyj = '" + shyj + "', ztid= " + ztid
					+ " where shid =" + shid;
			st.executeUpdate(task);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "ok";
	}

	// 获得指定新闻的审核记录(list)
	public static List<String[]> getSHByXWId(int xwid) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select shid, shrmc, shsj, shyj from shjl where shjl.xwid="
					+ xwid + " order by shid asc";
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[4];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				str[3] = rs.getString(4);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// 添加指定新闻的审核记录
	private static Object ADD_SHJL = new Object();

	public static String addSHByXWId(String xwid, String ygid, String fbsj,
			String ztid) {
		synchronized (ADD_SHJL) {
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			try {
				st = con.createStatement();
				String task = "select max(shid) from shjl";
				rs = st.executeQuery(task);
				rs.next();
				int maxSH = rs.getInt(1) + 1;
				String task1 = "insert into shjl values(" + maxSH + ", " + xwid
						+ " ," + ygid + ", '" + fbsj + "'," + null + ", "
						+ null + ", " + null + "," + ztid + " )";
				st.executeUpdate(task1);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 添加新闻
	private static Object ADD_LM = new Object();

	public static String addLM() {
		synchronized (ADD_LM) {
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			try {
				st = con.createStatement();
				String str = "select max(lmid) from lm";
				rs = st.executeQuery(str);
				rs.next();
				int max = rs.getInt(1) + 1;
				String sql = "insert into lm values(" + max
						+ " , '请在此处输入栏目名称', " + max + ")";
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 删除栏目
	private static Object DELETELM_LOCK = new Object();

	public static String deleteLM(int id) {

		synchronized (DELETELM_LOCK) {
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			try {
				st = con.createStatement();
				String task = "select xwid from xw where lmid =" + id;
				rs = st.executeQuery(task);
				if (rs.next()) {
					return "hasNew";
				} else {
					String sql = "delete from lm where lmid=" + id;
					st.executeUpdate(sql);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 调整栏目顺序(ok)
	private static Object Tran_LM = new Object();

	public static String tranLM(String sxid1, String lmid2, String sxid2,
			String lmid1) {
		synchronized (Tran_LM) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String sql = "update lm set sxid=" + sxid1 + " where lmid="
						+ lmid2;
				st.executeUpdate(sql);
				String sql1 = "update lm set sxid=" + sxid2 + " where lmid="
						+ lmid1;
				st.executeUpdate(sql1);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 调整新闻顺序(ok)
	private static Object Tran_XW = new Object();

	public static String tranXW(String sxid1, String xwid2, String sxid2,
			String xwid1) {
		synchronized (Tran_XW) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String sql = "update xw set sxid=" + sxid1 + " where xwid="
						+ xwid2;
				st.executeUpdate(sql);
				String sql1 = "update xw set sxid=" + sxid2 + " where xwid="
						+ xwid1;
				st.executeUpdate(sql1);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 修改栏目id
	private static Object xg_LMID = new Object();

	public static String xgLMID(int xwid, int lmid) {
		synchronized (xg_LMID) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String sql = "update xw set lmid= " + lmid + "  where xwid="
						+ xwid;
				st.executeUpdate(sql);
				String task = null;
				if (lmid == 0) {
					String sel="select max(sxid) from xw where lmid = 0 and fbztid = 0";
					ResultSet res=st.executeQuery(sel);
					if(res.next())
					{
						int max=res.getInt(1)+1;
						task = "update xw set fbztid = 0 , sxid = "+max+", lmid = 0  where xwid="
								+ xwid;
					}else
					{
						task = "update xw set fbztid = 0 , sxid = 1 ,lmid = 0 where xwid="
						+ xwid;
					}

				} else {
					String sel="select max(sxid) from xw where lmid = "+lmid+" and fbztid = 1";
					ResultSet res=st.executeQuery(sel);
					if(res.next())
					{
						int max=res.getInt(1)+1;
						task = "update xw set fbztid= 1 , sxid = "+max+" ,lmid = "+lmid+"  where xwid="
								+ xwid;
					}else
					{
						task = "update xw set fbztid= 1 , sxid = 1 , lmid = "+lmid+"  where xwid="
						+ xwid;
					}
				}
				st.executeUpdate(task);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 修改发布状态id
	private static Object xg_FBZTID = new Object();

	public static String xgFBZTID(int xwid, int fbztid) {
		synchronized (xg_FBZTID) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String sql = "update xw set fbztid= " + fbztid
						+ "  where xwid=" + xwid;
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}

	// 添加图片
	//private static Object ADD_PIC = new Object();

	public static String addPic(String tpms, String xwid, String tplj, int tplx)
	{
			Connection con = getConnection();
			Statement st = null;
			ResultSet rs = null;
			try {
				st = con.createStatement();
				String str = "select max(tpid) from tp";
				rs = st.executeQuery(str);
				rs.next();
				int max = rs.getInt(1) + 1;
				String sql = null;
				if (tplx == 0) {
					sql = "insert into tp values(" + max + " , null, " + xwid
							+ ",'" + tplj + "',0)";
				} else {
					sql = "insert into tp values(" + max + " , '" + tpms
							+ "', " + xwid + ",'" + tplj + "'," + tplx + ")";
				}
				st.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
	}

	// 获得指定的图片
	public static List<String[]> getPic(String xwid, String piclx) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select tplj, tpms, tplx from tp where xwid=" + xwid
					+ " and tplx = " + piclx;
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[3];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	// 更新图片
	private static Object UPDATE_PIC = new Object();
	public static String updatePic(String tpid,String tpms, String tplj)
	{
		synchronized (UPDATE_PIC) {
			Connection con = getConnection();
			Statement st = null;
			try {
				st = con.createStatement();
				String str = "update tp set tpms= '" +tpms 
						+ "' , tplj = '"+tplj+"'  where tpid = "+tpid;
				st.executeUpdate(str);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					st.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "ok";
		}
	}
	
	// 获得指定的图片id
	public static String getPicId(String xwid, int piclx) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		String res=null;
		try {
			st = con.createStatement();
			String task = "select tpid from tp where xwid=" + xwid
					+ " and tplx = " + piclx;
			rs = st.executeQuery(task);
			while (rs.next()) 
			{
                res=rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return res;
	}


	// *************************************for AD**********************
	// 获得栏目(list)
	public static List<String[]> getLMA() {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select lmid, lmmc, sxid from lm where lmid > 0 order by sxid asc";
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[4];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				Statement stm=con.createStatement();
				String sql="select count(*) from xw where lmid = "+str[0];
				ResultSet res = stm.executeQuery(sql);
				res.next();
				str[3]=res.getString(1);
				res.close();
				stm.close();
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

		
	// 获得栏目(list)
	public static List<String[]> getLMNewsA(String lmid,String startId,String lineSize) 
	{
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select xwid, xwbt, xwgs, sxid, xwly, fbsj from xw where lmid = "
					+ lmid +" order by sxid asc"+ " limit "+startId+" ,"+lineSize;
			rs = st.executeQuery(task);
			while (rs.next()) {
				String[] str = new String[6];
				str[0] = rs.getString(1);
				str[1] = rs.getString(2);
				str[2] = rs.getString(3);
				str[3] = rs.getString(4);
				str[4] = rs.getString(5);
				str[5] = rs.getString(6);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	// 获得新闻(list)
	public static List<String[]> getNEWA(String xwid) {
		Connection con = getConnection();
		Statement st = null;
		ResultSet rs = null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			st = con.createStatement();
			String task = "select bsid, xwnr  from xw where xwid = "+xwid;
			rs = st.executeQuery(task);
			rs.next();
			String[] str = new String[3];
			str[0] = xwid;
			str[1] = rs.getString(1);
			str[2] = rs.getString(2);
			list.add(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
