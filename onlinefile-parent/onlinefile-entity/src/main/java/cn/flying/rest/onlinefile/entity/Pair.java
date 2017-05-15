package cn.flying.rest.onlinefile.entity;


/**
 * 这是一个常用的成对数据. 
 * @version 1.0 
 */
@SuppressWarnings("unchecked")
public class Pair<T1, T2> implements  java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3171246365496137032L;
	/**
	 * 命名空间.
	 */
	public T1 a = null;
	/**
	 * 标识.
	 */
	public T2 b = null;
	/**
	 * 连接关系.
	 */
	public Object relation = null;
	/**
	 * 无参的构造方法.
	 */
	public Pair(){}
	/**
	 * 初始化参数的构造方法.
	 * @param t1 命名空间.
	 * @param t2 标识.
	 */
	public Pair( T1 t1, T2 t2 )
	{
	    a=t1;
	    b=t2;
	}
	
	public boolean equals(Object o){
		if(o == this)return true;
		if(o == null) return false;
		if(!(o instanceof Pair))return false;
		Pair p = (Pair)o;
		if( p.a == a && p.b == b ) return true;
	    if(a == null || b == null || p.a == null || p.b == null)return false;
	    return a.equals(p.a)&& b.equals(p.b);
	}
	
	public int hashCode()
	{
		if(a==null){
			if(b==null)return 0;
			return b.hashCode() ^ 789;
		}
		if(b==null)return a.hashCode() ^ 987;
	    return a.hashCode()^b.hashCode() ;
	}
	/**
	 * 复制.
	 * @return Pair<T1, T2> 返回拷贝后的pair对象.
	 */
	public Pair<T1, T2> clone(){
		Pair<T1, T2> p = new Pair<T1, T2>(a, b);
		p.relation = this.relation;
		return p;
	}
	/**
	 * 给命名空间设置值.
	 * @param _a 命名空间.
	 * @return Pair<T1, T2> 返回Pair<_a, null>.
	 */
	public Pair<T1, T2> setA(T1 _a){
		a = _a;
		return this;
	}
	/**
	 * 给标识设置值.
	 * @param _b 标识.
	 * @return Pair<T1, T2> 返回Pair<null, _b>.
	 */
	public Pair<T1, T2> setB(T2 _b){
		b = _b;
		return this;
	}
	/**
	 *  给命名空间和标识设置值.
	 * @param _a 命名空间.
	 * @param _b 标识.
	 * @return Pair<T1, T2> 返回Pair<_a, _b>.
	 */
	public Pair<T1, T2> set(T1 _a, T2 _b){
		a = _a;
		b = _b;
		return this;
	}
	/**
	 * 复制一份Pair.
	 * @param p Pair<T1,T2> 
	 * @return Pair<T1, T2>  返回复制的Pair.
	 */
	public Pair<T1, T2> copy(Pair<T1, T2> p){
		this.relation = p.relation;
		return set(p.a, p.b);
	}
	
	public String toString(){
		StringBuffer buffer = new StringBuffer("Pair.a=");
		buffer.append(this.a + "\r\n");
		buffer.append("Pair.b=" + this.b);
		return buffer.toString();
	}
	public static void main (String[] args) {
		Pair p = new Pair("a",null) ;
		System.out.print(p) ;
	}
}
