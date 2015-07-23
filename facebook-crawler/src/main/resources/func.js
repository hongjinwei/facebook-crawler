Int64 = function(g, h) {
	this.low_ = g;
	this.high_ = h;
};
Int64.IntCache_ = {};
Int64.fromInt = function(g) {
	if (-128 <= g && g < 128) {
		var h = Int64.IntCache_[g];
		if (h)
			return h;
	}
	var i = new Int64(g | 0, g < 0 ? -1 : 0);
	if (-128 <= g && g < 128)
		Int64.IntCache_[g] = i;
	return i;
};
Int64.fromNumber = function(g) {
	if (isNaN(g) || !isFinite(g)) {
		return Int64.ZERO;
	} else if (g <= -Int64.TWO_PWR_63_DBL_) {
		return Int64.MIN_VALUE;
	} else if (g + 1 >= Int64.TWO_PWR_63_DBL_) {
		return Int64.MAX_VALUE;
	} else if (g < 0) {
		return Int64.fromNumber(-g).negate();
	} else
		return new Int64((g % Int64.TWO_PWR_32_DBL_) | 0,
				(g / Int64.TWO_PWR_32_DBL_) | 0);
};
Int64.fromBits = function(g, h) {
	return new Int64(g, h);
};
Int64.fromString = function(g, h) {
	var k = Int64.ZERO;
	if (g.length == 0)
		return k;
	var i = h || 10;
	if (i < 2 || 36 < i)
		return k;
	if (g.charAt(0) == '-') {
		return Int64.fromString(g.substring(1), i).negate();
	} else if (g.indexOf('-') >= 0)
		return k;
	var j = Int64.fromNumber(Math.pow(i, 8));
	for (var l = 0; l < g.length; l += 8) {
		var m = Math.min(8, g.length - l), n = parseInt(g.substring(l, l + m),
				i);
		if (m < 8) {
			var o = Int64.fromNumber(Math.pow(i, m));
			k = k.multiply(o).add(Int64.fromNumber(n));
		} else {
			k = k.multiply(j);
			k = k.add(Int64.fromNumber(n));
		}
	}
	return k;
};
Int64.TWO_PWR_16_DBL_ = 1 << 16;
Int64.TWO_PWR_24_DBL_ = 1 << 24;
Int64.TWO_PWR_32_DBL_ = Int64.TWO_PWR_16_DBL_ * Int64.TWO_PWR_16_DBL_;
Int64.TWO_PWR_31_DBL_ = Int64.TWO_PWR_32_DBL_ / 2;
Int64.TWO_PWR_48_DBL_ = Int64.TWO_PWR_32_DBL_ * Int64.TWO_PWR_16_DBL_;
Int64.TWO_PWR_64_DBL_ = Int64.TWO_PWR_32_DBL_ * Int64.TWO_PWR_32_DBL_;
Int64.TWO_PWR_63_DBL_ = Int64.TWO_PWR_64_DBL_ / 2;
Int64.ZERO = Int64.fromInt(0);
Int64.ONE = Int64.fromInt(1);
Int64.NEG_ONE = Int64.fromInt(-1);
Int64.MAX_VALUE = Int64.fromBits(4294967295 | 0, 2147483647 | 0);
Int64.MIN_VALUE = Int64.fromBits(0, 2147483648 | 0);
Int64.TWO_PWR_24_ = Int64.fromInt(1 << 24);
Int64.prototype.toInt = function() {
	return this.low_;
};
Int64.prototype.toNumber = function() {
	return this.high_ * Int64.TWO_PWR_32_DBL_ + this.getLowBitsUnsigned();
};
Int64.prototype.toUnsignedBits = function() {
	var g = [], h = 31;
	for (var i = 32; i < 64; i++) {
		g[i] = (this.low_ >> h) & 1;
		h -= 1;
	}
	h = 31;
	for (var i = 0; i < 32; i++) {
		g[i] = (this.high_ >> h) & 1;
		h -= 1;
	}
	return g.join('');
};
Int64.prototype.toString = function(g) {
	var h = g || 10;
	if (h < 2 || 36 < h)
		return '0';
	if (this.isZero())
		return '0';
	if (this.isNegative())
		if (this.equals(Int64.MIN_VALUE)) {
			var i = Int64.fromNumber(h), j = this.div(i), k = j.multiply(i)
					.subtract(this);
			return j.toString(h) + k.toInt().toString(h);
		} else
			return '-' + this.negate().toString(h);
	var l = Int64.fromNumber(Math.pow(h, 6)), k = this, m = '';
	while (true) {
		var n = k.div(l), o = k.subtract(n.multiply(l)).toInt(), p = o
				.toString(h);
		k = n;
		if (k.isZero()) {
			return p + m;
		} else {
			while (p.length < 6)
				p = '0' + p;
			m = '' + p + m;
		}
	}
};
Int64.prototype.getHighBits = function() {
	return this.high_;
};
Int64.prototype.getLowBits = function() {
	return this.low_;
};
Int64.prototype.getLowBitsUnsigned = function() {
	return (this.low_ >= 0) ? this.low_ : Int64.TWO_PWR_32_DBL_ + this.low_;
};
Int64.prototype.getNumBitsAbs = function() {
	if (this.isNegative()) {
		if (this.equals(Int64.MIN_VALUE)) {
			return 64;
		} else
			return this.negate().getNumBitsAbs();
	} else {
		var g = this.high_ != 0 ? this.high_ : this.low_;
		for (var h = 31; h > 0; h--)
			if ((g & (1 << h)) != 0)
				break;
		return this.high_ != 0 ? h + 33 : h + 1;
	}
};
Int64.prototype.isZero = function() {
	return this.high_ == 0 && this.low_ == 0;
};
Int64.prototype.isNegative = function() {
	return this.high_ < 0;
};
Int64.prototype.isOdd = function() {
	return (this.low_ & 1) == 1;
};
Int64.prototype.equals = function(g) {
	return (this.high_ == g.high_) && (this.low_ == g.low_);
};
Int64.prototype.notEquals = function(g) {
	return (this.high_ != g.high_) || (this.low_ != g.low_);
};
Int64.prototype.lessThan = function(g) {
	return this.compare(g) < 0;
};
Int64.prototype.lessThanOrEqual = function(g) {
	return this.compare(g) <= 0;
};
Int64.prototype.greaterThan = function(g) {
	return this.compare(g) > 0;
};
Int64.prototype.greaterThanOrEqual = function(g) {
	return this.compare(g) >= 0;
};
Int64.prototype.compare = function(g) {
	if (this.equals(g))
		return 0;
	var h = this.isNegative(), i = g.isNegative();
	if (h && !i)
		return -1;
	if (!h && i)
		return 1;
	if (this.subtract(g).isNegative()) {
		return -1;
	} else
		return 1;
};
Int64.prototype.negate = function() {
	if (this.equals(Int64.MIN_VALUE)) {
		return Int64.MIN_VALUE;
	} else
		return this.not().add(Int64.ONE);
};
Int64.prototype.add = function(g) {
	var h = this.high_ >>> 16, i = this.high_ & 65535, j = this.low_ >>> 16, k = this.low_ & 65535, l = g.high_ >>> 16, m = g.high_ & 65535, n = g.low_ >>> 16, o = g.low_ & 65535, p = 0, q = 0, r = 0, s = 0;
	s += k + o;
	r += s >>> 16;
	s &= 65535;
	r += j + n;
	q += r >>> 16;
	r &= 65535;
	q += i + m;
	p += q >>> 16;
	q &= 65535;
	p += h + l;
	p &= 65535;
	return Int64.fromBits((r << 16) | s, (p << 16) | q);
};
Int64.prototype.subtract = function(g) {
	return this.add(g.negate());
};
Int64.prototype.multiply = function(g) {
	if (this.isZero()) {
		return Int64.ZERO;
	} else if (g.isZero())
		return Int64.ZERO;
	if (this.equals(Int64.MIN_VALUE)) {
		return g.isOdd() ? Int64.MIN_VALUE : Int64.ZERO;
	} else if (g.equals(Int64.MIN_VALUE))
		return this.isOdd() ? Int64.MIN_VALUE : Int64.ZERO;
	if (this.isNegative()) {
		if (g.isNegative()) {
			return this.negate().multiply(g.negate());
		} else
			return this.negate().multiply(g).negate();
	} else if (g.isNegative())
		return this.multiply(g.negate()).negate();
	if (this.lessThan(Int64.TWO_PWR_24_) && g.lessThan(Int64.TWO_PWR_24_))
		return Int64.fromNumber(this.toNumber() * g.toNumber());
	var h = this.high_ >>> 16, i = this.high_ & 65535, j = this.low_ >>> 16, k = this.low_ & 65535, l = g.high_ >>> 16, m = g.high_ & 65535, n = g.low_ >>> 16, o = g.low_ & 65535, p = 0, q = 0, r = 0, s = 0;
	s += k * o;
	r += s >>> 16;
	s &= 65535;
	r += j * o;
	q += r >>> 16;
	r &= 65535;
	r += k * n;
	q += r >>> 16;
	r &= 65535;
	q += i * o;
	p += q >>> 16;
	q &= 65535;
	q += j * n;
	p += q >>> 16;
	q &= 65535;
	q += k * m;
	p += q >>> 16;
	q &= 65535;
	p += h * o + i * n + j * m + k * l;
	p &= 65535;
	return Int64.fromBits((r << 16) | s, (p << 16) | q);
};
Int64.prototype.div = function(g) {
	if (g.isZero()) {
		return Int64.ZERO;
	} else if (this.isZero())
		return Int64.ZERO;
	if (this.equals(Int64.MIN_VALUE)) {
		if (g.equals(Int64.ONE) || g.equals(Int64.NEG_ONE)) {
			return Int64.MIN_VALUE;
		} else if (g.equals(Int64.MIN_VALUE)) {
			return Int64.ONE;
		} else {
			var h = this.shiftRight(1), i = h.div(g).shiftLeft(1);
			if (i.equals(Int64.ZERO)) {
				return g.isNegative() ? Int64.ONE : Int64.NEG_ONE;
			} else {
				var j = this.subtract(g.multiply(i)), k = i.add(j.div(g));
				return k;
			}
		}
	} else if (g.equals(Int64.MIN_VALUE))
		return Int64.ZERO;
	if (this.isNegative()) {
		if (g.isNegative()) {
			return this.negate().div(g.negate());
		} else
			return this.negate().div(g).negate();
	} else if (g.isNegative())
		return this.div(g.negate()).negate();
	var l = Int64.ZERO, j = this;
	while (j.greaterThanOrEqual(g)) {
		var i = Math.max(1, Math.floor(j.toNumber() / g.toNumber())), m = Math
				.ceil(Math.log(i) / Math.LN2), n = (m <= 48) ? 1 : Math.pow(2,
				m - 48), o = Int64.fromNumber(i), p = o.multiply(g);
		while (p.isNegative() || p.greaterThan(j)) {
			i -= n;
			o = Int64.fromNumber(i);
			p = o.multiply(g);
		}
		if (o.isZero())
			o = Int64.ONE;
		l = l.add(o);
		j = j.subtract(p);
	}
	return l;
};
Int64.prototype.modulo = function(g) {
	return this.subtract(this.div(g).multiply(g));
};
Int64.prototype.not = function() {
	return Int64.fromBits(~this.low_, ~this.high_);
};
Int64.prototype.and = function(g) {
	return Int64.fromBits(this.low_ & g.low_, this.high_ & g.high_);
};
Int64.prototype.or = function(g) {
	return Int64.fromBits(this.low_ | g.low_, this.high_ | g.high_);
};
Int64.prototype.xor = function(g) {
	return Int64.fromBits(this.low_ ^ g.low_, this.high_ ^ g.high_);
};
Int64.prototype.shiftLeft = function(g) {
	g &= 63;
	if (g == 0) {
		return this;
	} else {
		var h = this.low_;
		if (g < 32) {
			var i = this.high_;
			return Int64.fromBits(h << g, (i << g) | (h >>> (32 - g)));
		} else
			return Int64.fromBits(0, h << (g - 32));
	}
};
Int64.prototype.shiftRight = function(g) {
	g &= 63;
	if (g == 0) {
		return this;
	} else {
		var h = this.high_;
		if (g < 32) {
			var i = this.low_;
			return Int64.fromBits((i >>> g) | (h << (32 - g)), h >> g);
		} else
			return Int64.fromBits(h >> (g - 32), h >= 0 ? 0 : -1);
	}
};
Int64.prototype.shiftRightUnsigned = function(g) {
	g &= 63;
	if (g == 0) {
		return this;
	} else {
		var h = this.high_;
		if (g < 32) {
			var i = this.low_;
			return Int64.fromBits((i >>> g) | (h << (32 - g)), h >>> g);
		} else if (g == 32) {
			return Int64.fromBits(h, 0);
		} else
			return Int64.fromBits(h >>> (g - 32), 0);
	}
};

function QuickSandSolver() {
}
QuickSandSolver.prototype.solveAndEncode = function(m, n, o, p, q, r) {
	return JSON.stringify([ this.solve(m, n, o, p, q), r ]);
};
QuickSandSolver.prototype.solve = function(m, n, o, p, q) {
	var r = [], s = n;
	for (var t = 0; t < m; t++) {
		r.push(this.solveOneIteration(s, o, p, q));
		s = this.hashList(r[t]);
	}
	return r;
};
QuickSandSolver.prototype.solveOneIteration = function(m, n, o, p) {
	var q = 8192, r = new Encrypt(m, n), s = p * r.getSize() / 100, t = [], u = [], v = [];
	for (var w = 0; w < s; w++) {
		var x = r.sipEdge(w), y = x[0], z = x[1];
		y += 1;
		z += 1 + r.getHalfSize();
		var aa = t[y], ba = t[z];
		if (aa == z || ba == y)
			continue;
		u[0] = y;
		v[0] = z;
		var ca = this.path(aa, u, t, q), da = this.path(ba, v, t, q);
		if (u[ca] == v[da]) {
			var ea = Math.min(ca, da);
			for (ca -= ea, da -= ea; u[ca] != v[da]; ca++, da++)
				;
			var fa = ca + da + 1;
			if (fa == o)
				return this.recoverSolution(ca, da, u, v, r, o, s);
			continue;
		}
		if (ca < da) {
			while (ca--)
				t[u[ca + 1]] = u[ca];
			t[y] = z;
		} else {
			while (da--)
				t[v[da + 1]] = v[da];
			t[z] = y;
		}
	}
	return [];
};
QuickSandSolver.prototype.path = function(m, n, o, p) {
	var q = 0;
	for (q = 0; m; m = o[m]) {
		if (++q >= p) {
			while (q-- && n[q] != m)
				;
			if (q < 0) {
				throw "Maximum path length was exceeded";
			} else
				throw "Illegal cycle has occured";
		}
		n[q] = m;
	}
	return q;
};
QuickSandSolver.prototype.recoverSolution = function(m, n, o, p, q, r, s) {
	var t = function() {
	};
	t.prototype.add = function(z) {
		this[z] = true;
	};
	t.prototype.remove = function(z) {
		delete this[z];
	};
	var u = [], v = new t();
	v.add([ o[0], p[0] ]);
	while (m--)
		v.add([ o[(m + 1) & ~1], o[(m | 1)] ]);
	while (n--)
		v.add([ p[n | 1], p[(n + 1) & ~1] ]);
	var w = 0;
	for (var x = 0; x < s; x++) {
		var y = [ 1 + q.sipNode(x, 0), 1 + q.getHalfSize() + q.sipNode(x, 1) ];
		if (y in v) {
			u[w++] = x;
			v.remove(y);
		}
	}
	return u;
};
QuickSandSolver.prototype.hashList = function(m) {
	var n = m.join('-');
	return k(n);
}

function Encrypt(j, k) {
	this.$QuickSandHeader0 = 1 << k;
	this.$QuickSandHeader1 = this.$QuickSandHeader0 / 2;
	this.$QuickSandHeader2 = Int64.fromInt(this.$QuickSandHeader1 - 1);
	this.$QuickSandHeader3 = [];
	this.$QuickSandHeader4 = 0;
	this.$QuickSandHeader5 = 0;
	this.$QuickSandHeader6 = 0;
	this.$QuickSandHeader7 = 0;
	var l = ea(j), m = this.$QuickSandHeader8(this.$QuickSandHeader9(l)), n = this
			.$QuickSandHeader8(this.$QuickSandHeader9(l).slice(8));
	this.$QuickSandHeader3
			.push(m.xor(Int64.fromString('736f6d6570736575', 16)));
	this.$QuickSandHeader3
			.push(n.xor(Int64.fromString('646f72616e646f6d', 16)));
	this.$QuickSandHeader3
			.push(m.xor(Int64.fromString('6c7967656e657261', 16)));
	this.$QuickSandHeader3
			.push(n.xor(Int64.fromString('7465646279746573', 16)));
}
Encrypt.prototype.sipEdge = function(j) {
	return [ this.sipNode(j, 0), this.sipNode(j, 1) ];
};
Encrypt.prototype.sipNode = function(j, k) {
	return this.$QuickSandHeadera(2 * j + k).and(this.$QuickSandHeader2)
			.toInt();
};
Encrypt.prototype.getSize = function() {
	return this.$QuickSandHeader0;
};
Encrypt.prototype.getHalfSize = function() {
	return this.$QuickSandHeader1;
};
Encrypt.prototype.$QuickSandHeaderb = function(j, k) {
	return j.shiftLeft(k).or(j.shiftRightUnsigned(64 - k));
};
Encrypt.prototype.$QuickSandHeader8 = function(j) {
	var k = new Int64.fromInt(j[0]), l = Int64.fromInt(j[1]).shiftLeft(8), m = Int64
			.fromInt(j[2]).shiftLeft(16), n = Int64.fromInt(j[3]).shiftLeft(24), o = Int64
			.fromInt(j[4]).shiftLeft(32), p = Int64.fromInt(j[5]).shiftLeft(40), q = Int64
			.fromInt(j[6]).shiftLeft(48), r = Int64.fromInt(j[7]).shiftLeft(56);
	return k.or(l).or(m).or(n).or(o).or(p).or(q).or(r);
};
Encrypt.prototype.$QuickSandHeaderc = function() {
	this.$QuickSandHeader4 = this.$QuickSandHeader4.add(this.$QuickSandHeader5);
	this.$QuickSandHeader6 = this.$QuickSandHeader6.add(this.$QuickSandHeader7);
	this.$QuickSandHeader5 = this.$QuickSandHeaderb(this.$QuickSandHeader5, 13);
	this.$QuickSandHeader7 = this.$QuickSandHeaderb(this.$QuickSandHeader7, 16);
	this.$QuickSandHeader5 = this.$QuickSandHeader5.xor(this.$QuickSandHeader4);
	this.$QuickSandHeader7 = this.$QuickSandHeader7.xor(this.$QuickSandHeader6);
	this.$QuickSandHeader4 = this.$QuickSandHeaderb(this.$QuickSandHeader4, 32);
	this.$QuickSandHeader6 = this.$QuickSandHeader6.add(this.$QuickSandHeader5);
	this.$QuickSandHeader4 = this.$QuickSandHeader4.add(this.$QuickSandHeader7);
	this.$QuickSandHeader5 = this.$QuickSandHeaderb(this.$QuickSandHeader5, 17);
	this.$QuickSandHeader7 = this.$QuickSandHeaderb(this.$QuickSandHeader7, 21);
	this.$QuickSandHeader5 = this.$QuickSandHeader5.xor(this.$QuickSandHeader6);
	this.$QuickSandHeader7 = this.$QuickSandHeader7.xor(this.$QuickSandHeader4);
	this.$QuickSandHeader6 = this.$QuickSandHeaderb(this.$QuickSandHeader6, 32);
};
Encrypt.prototype.$QuickSandHeader9 = function(j) {
	for (var k = [], l = 0; l < j.length; l += 2)
		k.push(parseInt(j.substr(l, 2), 16));
	return k;
};
Encrypt.prototype.$QuickSandHeadera = function(j) {
	var k = Int64.fromInt(j);
	this.$QuickSandHeader4 = this.$QuickSandHeader3[0];
	this.$QuickSandHeader5 = this.$QuickSandHeader3[1];
	this.$QuickSandHeader6 = this.$QuickSandHeader3[2];
	this.$QuickSandHeader7 = this.$QuickSandHeader3[3].xor(k);
	for (var l = 0; l < 2; l++)
		this.$QuickSandHeaderc();
	this.$QuickSandHeader4 = this.$QuickSandHeader4.xor(k);
	this.$QuickSandHeader6 = this.$QuickSandHeader6.xor(Int64.fromString('ff',
			16));
	for (l = 0; l < 4; l++)
		this.$QuickSandHeaderc();
	return this.$QuickSandHeader4.xor(this.$QuickSandHeader5).xor(
			this.$QuickSandHeader6).xor(this.$QuickSandHeader7);
};

function h(fa, ga) {
	return ((ga >>> fa) | (ga << (32 - fa)));
}
function i(fa, ga, ha) {
	return ((fa & ga) ^ (~fa & ha));
}
function j(fa, ga, ha) {
	return ((fa & ga) ^ (fa & ha) ^ (ga & ha));
}
function k(fa) {
	return (h(2, fa) ^ h(13, fa) ^ h(22, fa));
}
function l(fa) {
	return (h(6, fa) ^ h(11, fa) ^ h(25, fa));
}
function m(fa) {
	return (h(7, fa) ^ h(18, fa) ^ (fa >>> 3));
}
function n(fa) {
	return (h(17, fa) ^ h(19, fa) ^ (fa >>> 10));
}
function o(fa, ga) {
	return (fa[ga & 15] += n(fa[(ga + 14) & 15]) + fa[(ga + 9) & 15]
			+ m(fa[(ga + 1) & 15]));
}
var p = new Array(1116352408, 1899447441, 3049323471, 3921009573, 961987163,
		1508970993, 2453635748, 2870763221, 3624381080, 310598401, 607225278,
		1426881987, 1925078388, 2162078206, 2614888103, 3248222580, 3835390401,
		4022224774, 264347078, 604807628, 770255983, 1249150122, 1555081692,
		1996064986, 2554220882, 2821834349, 2952996808, 3210313671, 3336571891,
		3584528711, 113926993, 338241895, 666307205, 773529912, 1294757372,
		1396182291, 1695183700, 1986661051, 2177026350, 2456956037, 2730485921,
		2820302411, 3259730800, 3345764771, 3516065817, 3600352804, 4094571909,
		275423344, 430227734, 506948616, 659060556, 883997877, 958139571,
		1322822218, 1537002063, 1747873779, 1955562222, 2024104815, 2227730452,
		2361852424, 2428436474, 2756734187, 3204031479, 3329325298), q = new Array(
		8), r = new Array(2), s = new Array(64), t = new Array(16), u = "0123456789abcdef";
function v(fa, ga) {
	var ha = (fa & 65535) + (ga & 65535), ia = (fa >> 16) + (ga >> 16)
			+ (ha >> 16);
	return (ia << 16) | (ha & 65535);
}
function w() {
	r[0] = r[1] = 0;
	q[0] = 1779033703;
	q[1] = 3144134277;
	q[2] = 1013904242;
	q[3] = 2773480762;
	q[4] = 1359893119;
	q[5] = 2600822924;
	q[6] = 528734635;
	q[7] = 1541459225;
}
function x() {
	var fa, ga, ha, ia, ja, ka, la, ma, na, oa;
	fa = q[0];
	ga = q[1];
	ha = q[2];
	ia = q[3];
	ja = q[4];
	ka = q[5];
	la = q[6];
	ma = q[7];
	for (var pa = 0; pa < 16; pa++)
		t[pa] = ((s[(pa << 2) + 3]) | (s[(pa << 2) + 2] << 8)
				| (s[(pa << 2) + 1] << 16) | (s[pa << 2] << 24));
	for (var qa = 0; qa < 64; qa++) {
		na = ma + l(ja) + i(ja, ka, la) + p[qa];
		if (qa < 16) {
			na += t[qa];
		} else
			na += o(t, qa);
		oa = k(fa) + j(fa, ga, ha);
		ma = la;
		la = ka;
		ka = ja;
		ja = v(ia, na);
		ia = ha;
		ha = ga;
		ga = fa;
		fa = v(na, oa);
	}
	q[0] += fa;
	q[1] += ga;
	q[2] += ha;
	q[3] += ia;
	q[4] += ja;
	q[5] += ka;
	q[6] += la;
	q[7] += ma;
}
function y(fa, ga) {
	var ha, ia, ja = 0;
	ia = ((r[0] >> 3) & 63);
	var ka = (ga & 63);
	if ((r[0] += (ga << 3)) < (ga << 3))
		r[1]++;
	r[1] += (ga >> 29);
	for (ha = 0; ha + 63 < ga; ha += 64) {
		for (var la = ia; la < 64; la++)
			s[la] = fa.charCodeAt(ja++);
		x();
		ia = 0;
	}
	for (var la = 0; la < ka; la++)
		s[la] = fa.charCodeAt(ja++);
}
function z() {
	var fa = ((r[0] >> 3) & 63);
	s[fa++] = 128;
	if (fa <= 56) {
		for (var ga = fa; ga < 56; ga++)
			s[ga] = 0;
	} else {
		for (var ga = fa; ga < 64; ga++)
			s[ga] = 0;
		x();
		for (var ga = 0; ga < 56; ga++)
			s[ga] = 0;
	}
	s[56] = (r[1] >>> 24) & 255;
	s[57] = (r[1] >>> 16) & 255;
	s[58] = (r[1] >>> 8) & 255;
	s[59] = r[1] & 255;
	s[60] = (r[0] >>> 24) & 255;
	s[61] = (r[0] >>> 16) & 255;
	s[62] = (r[0] >>> 8) & 255;
	s[63] = r[0] & 255;
	x();
}
function aa() {
	var fa = 0, ga = new Array(32);
	for (var ha = 0; ha < 8; ha++) {
		ga[fa++] = ((q[ha] >>> 24) & 255);
		ga[fa++] = ((q[ha] >>> 16) & 255);
		ga[fa++] = ((q[ha] >>> 8) & 255);
		ga[fa++] = (q[ha] & 255);
	}
	return ga;
}
function ba() {
	var fa = new String();
	for (var ga = 0; ga < 8; ga++)
		for (var ha = 28; ha >= 0; ha -= 4)
			fa += u.charAt((q[ga] >>> ha) & 15);
	return fa;
}
function ca(fa) {
	var ga = 0;
	for (var ha = 0; ha < 8; ha++)
		for (var ia = 28; ia >= 0; ia -= 4)
			fa[ga++] = u.charCodeAt((q[ha] >>> ia) & 15);
}
function da(fa, ga) {
	w();
	y(fa, fa.length);
	z();
	if (ga) {
		ca(ga);
	} else
		return ba();
}
function ea(fa, ga, ha) {
	if (fa === null || fa === (void 0))
		return null;
	ga = (typeof ga == 'undefined') ? true : ga;
	if (ga)
		fa = g(fa);
	return da(fa, ha);
}

function g(h) {
	var i = "", j, k;
	for (var l = 0; l < h.length; l++) {
		j = h.charCodeAt(l);
		k = l + 1 < h.length ? h.charCodeAt(l + 1) : 0;
		if (55296 <= j && j <= 56319 && 56320 <= k && k <= 57343) {
			j = 65536 + ((j & 1023) << 10) + (k & 1023);
			l++;
		}
		if (j <= 127) {
			i += String.fromCharCode(j);
		} else if (j <= 2047) {
			i += String.fromCharCode(192 | ((j >>> 6) & 31), 128 | (j & 63));
		} else if (j <= 65535) {
			i += String.fromCharCode(224 | ((j >>> 12) & 15),
					128 | ((j >>> 6) & 63), 128 | (j & 63));
		} else if (j <= 2097151)
			i += String.fromCharCode(240 | ((j >>> 18) & 7),
					128 | ((j >>> 12) & 63), 128 | ((j >>> 6) & 63),
					128 | (j & 63));
	}
	return i;
}

if(typeof JSON!=="object"){JSON={}}(function(){var rx_one=/^[\],:{}\s]*$/,rx_two=/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,rx_three=/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,rx_four=/(?:^|:|,)(?:\s*\[)+/g,rx_escapable=/[\\\"\u0000-\u001f\u007f-\u009f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,rx_dangerous=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g;function f(n){return n<10?"0"+n:n}function this_value(){return this.valueOf()}if(typeof Date.prototype.toJSON!=="function"){Date.prototype.toJSON=function(){return isFinite(this.valueOf())?this.getUTCFullYear()+"-"+f(this.getUTCMonth()+1)+"-"+f(this.getUTCDate())+"T"+f(this.getUTCHours())+":"+f(this.getUTCMinutes())+":"+f(this.getUTCSeconds())+"Z":null};Boolean.prototype.toJSON=this_value;Number.prototype.toJSON=this_value;String.prototype.toJSON=this_value}var gap,indent,meta,rep;function quote(string){rx_escapable.lastIndex=0;return rx_escapable.test(string)?'"'+string.replace(rx_escapable,function(a){var c=meta[a];return typeof c==="string"?c:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+string+'"'}function str(key,holder){var i,k,v,length,mind=gap,partial,value=holder[key];if(value&&typeof value==="object"&&typeof value.toJSON==="function"){value=value.toJSON(key)}if(typeof rep==="function"){value=rep.call(holder,key,value)}switch(typeof value){case"string":return quote(value);case"number":return isFinite(value)?String(value):"null";case"boolean":case"null":return String(value);case"object":if(!value){return"null"}gap+=indent;partial=[];if(Object.prototype.toString.apply(value)==="[object Array]"){length=value.length;for(i=0;i<length;i+=1){partial[i]=str(i,value)||"null"}v=partial.length===0?"[]":gap?"[\n"+gap+partial.join(",\n"+gap)+"\n"+mind+"]":"["+partial.join(",")+"]";gap=mind;return v}if(rep&&typeof rep==="object"){length=rep.length;for(i=0;i<length;i+=1){if(typeof rep[i]==="string"){k=rep[i];v=str(k,value);if(v){partial.push(quote(k)+(gap?": ":":")+v)}}}}else{for(k in value){if(Object.prototype.hasOwnProperty.call(value,k)){v=str(k,value);if(v){partial.push(quote(k)+(gap?": ":":")+v)}}}}v=partial.length===0?"{}":gap?"{\n"+gap+partial.join(",\n"+gap)+"\n"+mind+"}":"{"+partial.join(",")+"}";gap=mind;return v}}if(typeof JSON.stringify!=="function"){meta={"\b":"\\b","\t":"\\t","\n":"\\n","\f":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"};JSON.stringify=function(value,replacer,space){var i;gap="";indent="";if(typeof space==="number"){for(i=0;i<space;i+=1){indent+=" "}}else{if(typeof space==="string"){indent=space}}rep=replacer;if(replacer&&typeof replacer!=="function"&&(typeof replacer!=="object"||typeof replacer.length!=="number")){throw new Error("JSON.stringify")}return str("",{"":value})}}if(typeof JSON.parse!=="function"){JSON.parse=function(text,reviver){var j;function walk(holder,key){var k,v,value=holder[key];if(value&&typeof value==="object"){for(k in value){if(Object.prototype.hasOwnProperty.call(value,k)){v=walk(value,k);if(v!==undefined){value[k]=v}else{delete value[k]}}}}return reviver.call(holder,key,value)}text=String(text);rx_dangerous.lastIndex=0;if(rx_dangerous.test(text)){text=text.replace(rx_dangerous,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})}if(rx_one.test(text.replace(rx_two,"@").replace(rx_three,"]").replace(rx_four,""))){j=eval("("+text+")");return typeof reviver==="function"?walk({"":j},""):j}throw new SyntaxError("JSON.parse")}}}());

function solveAndEncode(m, n, o, p, q, s) {
	var s = new QuickSandSolver();
	return s.solveAndEncode(m, n, o, p, q, s)
}