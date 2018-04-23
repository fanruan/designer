package com.fr.design.designer.beans;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.PriorityQueue;

import com.fr.form.ui.container.WLayout;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;

public class ConnectorCreator {
	
	public static final int UNIT = 10;
	public static final int SIDE = 2;
	public static final int CORNER_LOSS = 20;
	public static final int VECTOR[][] = { { UNIT, 0 }, { -UNIT, 0 }, { 0, UNIT }, { 0, -UNIT } };
	
	private long timeOut = 200;
	private boolean beyond;
	private WLayout container;
	private BoundsWidget IgnoreLayout;
	private Point startPoint;
	private Point endPoint;
	private PriorityQueue<AssessedPoint> open = new PriorityQueue<AssessedPoint>();
	private PriorityQueue<AssessedPoint> close = new PriorityQueue<AssessedPoint>();
	
	public ConnectorCreator(WLayout container, Point startPoint, Point endPoint) {
		this.container =container;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		if (getNearWidget(this.endPoint, SIDE * UNIT - 1) != null) {
			if ((IgnoreLayout = getNearWidget(this.endPoint, -1)) == null) {
				beyond = true;
			}
		}
	}
	
	private static int difference(int x, int y) {
		int p;
		if (x < y) {
			p = x;
			x = y;
			y = p;
		}
		return x - y;
	}
	
	public static int getMinimumDistance(Point A, Point B) {
		return difference(A.x, B.x) + difference(A.y, B.y) + (A.x == B.x || A.y == B.y ? 0 : CORNER_LOSS);
	}
	
	private BoundsWidget getNearWidget(Point endPoint, int l) {
		Rectangle[] r = new Rectangle[container.getWidgetCount()];
		Rectangle temp = new Rectangle();
		BoundsWidget widget;
		for (int i = 0, size = r.length; i < size; i++) {
			widget = ((BoundsWidget) container.getWidget(i));
			if (widget.isVisible()) {
				r[i] = widget.getBounds();
				temp.setBounds(r[i]);
				temp.grow(l, l);
				if (inside(endPoint, temp)) {
					return widget;
				}
			}
		}
		return null;
	}
	
	private boolean arrive(Point p1, Point p2) {
		if (!beyond) {
			return p1.x - p2.x < UNIT && p2.x - p1.x < UNIT && p1.y - p2.y < UNIT && p2.y - p1.y < UNIT;
		} else {
			return p1.x - p2.x < (SIDE +1)* UNIT && p2.x - p1.x < (SIDE +1) * UNIT && p1.y - p2.y < (SIDE +1)* UNIT && p2.y - p1.y < (SIDE +1) * UNIT;
		}
	}

	private boolean inside(Point p, Rectangle r) {
		return p.x >= r.x && p.x <= r.x + r.width && p.y >= r.y && p.y <= r.y + r.height;
	}

	private boolean check(Point p) {
		if (p.x <= 0 || p.y <= 0) {
			return false;
		}
		
		BoundsWidget bw = getNearWidget(p, SIDE * UNIT - 1);
		return bw == IgnoreLayout || bw ==null;
	}
	
	public ArrayList<Point> createPointList() {
		ArrayList<Point> l = new ArrayList<Point>();
		AssessedPoint pst = new AssessedPoint(startPoint,null,false);
		AssessedPoint temp ;
		long startTime = System.currentTimeMillis();
		open.add(pst);
		while ((temp = open.poll()) != null || (checkClose() && (temp = open.poll()) != null)) {
			if (arrive(temp.p, endPoint)) {
				temp.getS();
				l.addAll(temp.pointList);
				return l;
			} else {
				close.add(temp);
				temp.pushInto();
			}
			if (System.currentTimeMillis() - startTime > timeOut) {
				break;
			}
		}
		l.add(startPoint);
		l.add(new Point(startPoint.x,endPoint.y));
		l.add(endPoint);
		return l;
	}
	
	private boolean checkClose() {
		if(close.size() == 1) {
			AssessedPoint p = close.poll();
			return p.reCheck();
		}
		return false;
	}
	
	class AssessedPoint implements Comparable<AssessedPoint>{
		ArrayList<Point> pointList;
		Point p;
		Point parent;
		int distance;
		int g;
		
		AssessedPoint(Point p, AssessedPoint parent, boolean loss) {
			this.p = p;
			pointList = new ArrayList<Point>();
			if (parent != null) {
				this.g = parent.g + (loss ? CORNER_LOSS : UNIT);
				this.parent = parent.p;
				pointList.addAll(parent.pointList);
				if (loss) {
					pointList.add(parent.p);
				}
			} else {
				pointList.add(p);
				g = 0;
			}
			this.distance = getMinimumDistance(p, endPoint) + g;
		}

		public void getS() {
			int size = pointList.size();
			if(size > 1) {
				Point p1 = pointList.get(size - 1);
				if(p1.x == p.x) {
					if(endPoint.x != p1.x) {
						if(beyond) {
							pointList.add(new Point(p1.x,endPoint.y));
						} else {
							p1.x = p.x = endPoint.x;
						}
						
					}
				} else if(p1.y == p.y) {
					if(endPoint.y != p1.y) {
						if(beyond) {
							pointList.add(new Point(endPoint.x,p1.y));
						} else {
							p1.y = p.y = endPoint.y;
						}
					}
				}
			} else if (size == 1 && (startPoint.x != endPoint.x || startPoint.y != endPoint.y)) {
				pointList.add(new Point(startPoint.x, endPoint.y));
			}
			pointList.add(endPoint);
		}

		public int compareTo(AssessedPoint o) {
			return distance - o.distance;
		}

		void pushInto() {
			for (int i = 0; i < VECTOR.length; i++) {
				Point temp = new Point(p.x + VECTOR[i][0], p.y + VECTOR[i][1]);
				if (parent != null && parent.x == temp.x && parent.y == temp.y) {
					continue;
				}
				AssessedPoint ap = new AssessedPoint(temp, this, loss(temp));
				if (check(temp) && !open.contains(ap) && !close.contains(ap)) {
					open.add(ap);
				}
			}
		}
		
		boolean reCheck() {
			for (int i = 0; i < VECTOR.length; i++) {
				Point temp = new Point(p.x + SIDE * VECTOR[i][0], p.y + SIDE * VECTOR[i][1]);
				AssessedPoint ap = new AssessedPoint(temp, this, loss(temp));
				if (check(temp)) {
					open.add(ap);
				}
			}
			return open.size() != 0;
		}

		private boolean loss(Point temp) {
			return (parent != null && ((p.x == parent.x && temp.x != p.x) || (p.y == parent.y && temp.y != p.y)));
		}

		@Override
		public boolean equals(Object o) {
			return o instanceof AssessedPoint && ((AssessedPoint) o).p.x == p.x && ((AssessedPoint) o).p.y == p.y;
		}
	}

}