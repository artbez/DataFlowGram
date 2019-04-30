import numpy as np
import matplotlib.pyplot as plt

x = 3
print("hoho")
t1 = np.arange(x)
t2 = np.arange(x)
l = plt.plot(t1, t2, 'ro')
plt.setp(l, markersize=30)
plt.setp(l, markerfacecolor='C0')
plt.savefig('foo.png')