import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.SECONDS;


public class PDU {

	private byte[] SMS;
	private LocalTime time;
	private long seconds;
	private String message;
	private VRD vrd;

	public String getMessage() {
		return message;
	}

	public void setVrd(VRD vrd) {
		this.vrd = vrd;
	}

	public void setSMS(byte[] SMS) {
		this.SMS = SMS;
	}

	public VRD getVrd() {
		return vrd;
	}

	public PDU(String message) {
		this.message=message;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	boolean timePassed() {
		return (time.until(LocalTime.now(), SECONDS) > this.seconds) ;
	}

	public LocalTime getTime() {
		return time;
	}

	public long getSeconds() {
		return seconds;
	}
}
