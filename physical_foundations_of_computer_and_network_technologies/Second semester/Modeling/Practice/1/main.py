import numpy
import matplotlib.pyplot
import matplotlib
matplotlib.use('TkAgg')
from scipy.integrate import solve_ivp
from scipy.signal import find_peaks

# Параметры моделирования
modelling_time = 20  # время моделирования [c]

# Параметры системы
L = 1.0  # [м]
m = 1.0  # [кг]
k = 1.0  # [Н/м]
L1 = 1.0  # [м]
g = 9.81  # [м/с^2]
beta = 0.05  # коэффициент сопротивления

# Начальные условия
phi1_0 = 0.3  # начальный угол первого маятника [рад]
dphi1_0 = 0.0  # начальная угловая скорость первого маятника [рад/с]
phi2_0 = -0.3  # начальный угол второго маятника [рад]
dphi2_0 = 0.0  # начальная угловая скорость второго маятника [рад/с]

# Система дифференциальных уравнений
def system(t, y):
    phi1, dphi1, phi2, dphi2 = y  # Текущие параметры
    spring = k * (phi2 - phi1) * L1 ** 2 / (m * L ** 2)  # Момент силы (сила упругости пружины) (1)
    ddphi1 = - (g / L) * numpy.sin(phi1) - beta * dphi1 + spring  # Уравнение движения для маятника 1 (2)
    ddphi2 = - (g / L) * numpy.sin(phi2) - beta * dphi2 - spring  # Уравнение движения для маятника 2 (2)
    return [dphi1, ddphi1, dphi2, ddphi2]

# Моделирование
t_span = (0, modelling_time)
t_eval = numpy.linspace(*t_span, 1000)
y0 = [phi1_0, dphi1_0, phi2_0, dphi2_0] # начальные условие для маятников

sol = solve_ivp(system, t_span, y0, t_eval=t_eval, method='RK45') # решение системы уравнений (по методу Рунге-Кутты)

# Результаты
phi1 = sol.y[0]
dphi1 = sol.y[1]
phi2 = sol.y[2]
dphi2 = sol.y[3]
time = sol.t

# Построение графика углов
matplotlib.pyplot.figure(figsize=(10, 5))
matplotlib.pyplot.plot(time, phi1, label='phi_1', color='black')
matplotlib.pyplot.plot(time, phi2, label='phi_2', color='blue')
matplotlib.pyplot.xlabel('Время (с)')
matplotlib.pyplot.ylabel('Угол (рад)')
matplotlib.pyplot.title('Зависимость углов от времени')
matplotlib.pyplot.grid(True)
matplotlib.pyplot.legend()
matplotlib.pyplot.show()

# Построение графика скоростей
matplotlib.pyplot.figure(figsize=(10, 5))
matplotlib.pyplot.plot(time, dphi1, label='dphi_1/dt', color='black')
matplotlib.pyplot.plot(time, dphi2, label='dphi_2/dt', color='blue')
matplotlib.pyplot.xlabel('Время (с)')
matplotlib.pyplot.ylabel('Угловая скорость (рад/с)')
matplotlib.pyplot.title('Зависимость угловых скоростей от времени')
matplotlib.pyplot.grid(True)
matplotlib.pyplot.legend()
matplotlib.pyplot.show()

# Поиск нормальных частот
# Поиск точек локального максимума для определения периода колебаний
peaks1, _ = find_peaks(phi1)
peaks2, _ = find_peaks(phi2)

# Поиск периода между локальными максимума
periods1 = numpy.diff(time[peaks1])
periods2 = numpy.diff(time[peaks2])

# Поиск средних периодов и частот
T1 = numpy.mean(periods1)
T2 = numpy.mean(periods2)
omega1 = 2 * numpy.pi / T1
omega2 = 2 * numpy.pi / T2

print("Нормальная частота phi_1 = {:.3f} рад/с".format(omega1))
print("Нормальная частота phi_2 = {:.3f} рад/с".format(omega2))