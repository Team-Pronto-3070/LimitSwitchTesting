package org.usfirst.frc.team3070.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

public class TestLift {

	interface LiftState {
		public LiftState check();
	}

	private static final double LIFT_SPEED = .6;
	
	static CANTalon motor1, motor2;
	static Joystick xbox;
	static DigitalInput lower, upper, tote;
	static boolean atTop, atBottom, readyForNextTote;
	static int toteCount;
	LiftState state;

	public TestLift(CANTalon m1, CANTalon m2, DigitalInput u, DigitalInput l,
			DigitalInput t, Joystick x) {
		motor1 = m1;
		motor2 = m2;
		lower = l;
		upper = u;
		tote = t;
		xbox = x;
		atTop = false;
		atBottom = false;
		toteCount = 0;
		
		state = LiftStates.Stopped;
	}

	enum LiftStates implements LiftState {
		Stopped {
			@Override
			public LiftState check() {
				if (atTop && xbox.getRawButton(1)) {
					return StartLiftUp;
				}

				if (atBottom && xbox.getRawButton(2)) {
					return StartLiftDown;
				}

				return Stopped;
			}
		},
		StartLiftUp {
			@Override
			public LiftState check() {
				lift(LIFT_SPEED);
				atBottom = false;
				return LiftingUp;
			}
		},
		LiftingUp {
			@Override
			public LiftState check() {
				if (!upper.get()) {
					atTop = true;
					return Stopping;
				}
				
				if (!tote.get()) {
					return WaitForRelease;
				}
				
				if (!xbox.getRawButton(1)) {
					return Stopping;
				}
				
				return LiftingUp;
			}
		},
		WaitForRelease {
			@Override
			public LiftState check() {
				if (!tote.get()) {
					lift(.25);
				} else {
					lift(0);
				}
				if (!xbox.getRawButton(1)) {
					toteCount++;
					return Stopping;
				}
				
				return WaitForRelease;
			}
		},
		StartLiftDown {
			@Override
			public LiftState check() {
				lift(-LIFT_SPEED);
				atTop = false;
				return LiftingDown;
			}
		},
		LiftingDown {
			@Override
			public LiftState check() {
				if (!lower.get() && toteCount == 1) {
					atBottom = true;
					return Stopping;
				}
				
				if (!lower.get()) {
					return MoveDownPastSwitch;
				}
				
				if (!xbox.getRawButton(2))
					return Stopping;
				return LiftingDown;
			}
		},
		MoveDownPastSwitch {
			@Override
			public LiftState check() {
				if (!lower.get()) {
					lift(-LIFT_SPEED);
				} else {
					toteCount--;
					return LiftingDown;
				}
				return MoveDownPastSwitch;
			}
		},
		Stopping {
			@Override
			public LiftState check() {
				lift(0);
				return Stopped;
			}
		}
	}
	
	public void periodic() {
		state = state.check();
	}
	
	public void stopPeriodic() {
		lift(0);
	}
	
	private static void lift(double speed) {
		motor1.set(speed);
		motor2.set(-speed);
	}

}
