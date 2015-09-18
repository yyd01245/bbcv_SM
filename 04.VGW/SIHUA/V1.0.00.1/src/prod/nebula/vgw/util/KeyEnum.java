package prod.nebula.vgw.util;

/**
 * 
 * @author Chenmm
 * 
 *         键值通信报文枚举值
 * 
 */

public class KeyEnum {

	/**
	 * 接入设备类型 1001 ：遥控器设备
	 */
	public enum KeyDevType {

		IrrControl(1001);

		private int devType;

		private KeyDevType(int devType) {
			this.devType = devType;
		}

		public int getDevType() {
			return devType;
		}

	}

	/**
	 * 遥控器键值状态 3 ：释放 4 ：按下
	 */
	public enum IrrKeyStat {
		IrrStatUp(3), IrrStatDown(4);

		private int status;

		private IrrKeyStat(int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}
	}

}
