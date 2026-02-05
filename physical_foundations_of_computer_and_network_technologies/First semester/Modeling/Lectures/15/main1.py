import numpy as np
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import tkinter as tk
from tkinter import ttk

# Функция для расчета токов
def calculate_currents(resistors, sources):
    n = len(resistors)
    A = np.zeros((n, n))
    B = np.array(sources)

    for i in range(n):
        A[i, i] = resistors[i]
        if i > 0:
            A[i, i - 1] = -resistors[i]
            A[i - 1, i] = -resistors[i]

    currents = np.linalg.solve(A, B)
    return currents

def draw_circuit(canvas_frame, resistors, sources):
    fig, ax = plt.subplots(figsize=(6, 4))

    x, y = 0, 0
    for i, (res, src) in enumerate(zip(resistors, sources)):
        ax.plot([x, x + 2], [y, y], color="black", lw=2)
        ax.text(x + 1, y + 0.2, f"R{i+1}={res} Ω", fontsize=6, ha="center")
        if src:
            ax.arrow(x + 0.5, y + 0.5, 0, -1, head_width=0.1, head_length=0.2, fc="red", ec="red")
            ax.text(x + 0.5, y + 0.8, f"E{i+1}={src}V", color="red", ha="center")
        x += 2

    ax.set_xlim(-1, x + 1)
    ax.set_ylim(-2, 2)
    ax.axis('off')
    plt.title("Electrical Circuit", fontsize=16)

    canvas = FigureCanvasTkAgg(fig, master=canvas_frame)
    canvas.get_tk_widget().pack()
    canvas.draw()

def main_window():
    def add_element():
        res = resistance_entry.get()
        src = source_entry.get()
        try:
            res = float(res)
            src = float(src)
            if res <= 0:
                raise ValueError("Resistance must be positive.")
            resistors.append(res)
            sources.append(src)
            elements_list.insert(tk.END, f"R={res}Ω, E={src}V")
            resistance_entry.delete(0, tk.END)
            source_entry.delete(0, tk.END)
        except ValueError:
            result_label.config(text="Invalid input! Ensure valid numbers.", foreground="red")

    def calculate_and_draw():
        for widget in circuit_frame.winfo_children():
            widget.destroy()

        if resistors and sources:
            try:
                currents = calculate_currents(resistors, sources)
                draw_circuit(circuit_frame, resistors, sources)

                result_text = "Currents:\n"
                for i, current in enumerate(currents):
                    result_text += f"I{i+1} = {current:.2f} A\n"
                result_label.config(text=result_text, foreground="white")
            except Exception as e:
                result_label.config(text=f"Error: {str(e)}", foreground="red")
        else:
            result_label.config(text="Add elements to the circuit first.", foreground="red")

    root = tk.Tk()
    root.title("Circuit Solver")

    background_color = "#0773a1"
    root.configure(bg=background_color)

    style = ttk.Style()
    style.theme_use("default")

    style.configure("TFrame", background=background_color)
    style.configure("TLabel", background=background_color, foreground="white")
    style.configure("TButton", background="#045478", foreground="white")
    style.configure("TEntry", fieldbackground="white", background="white")

    main_frame = ttk.Frame(root, padding="10")
    main_frame.grid(row=0, column=0, sticky=(tk.W, tk.E, tk.N, tk.S))

    input_frame = ttk.Frame(main_frame, padding="5")
    input_frame.grid(row=0, column=0, sticky=tk.W)

    ttk.Label(input_frame, text="Resistance (Ω):").grid(row=0, column=0, sticky=tk.W)
    resistance_entry = ttk.Entry(input_frame, width=10)
    resistance_entry.grid(row=0, column=1, sticky=tk.W)

    ttk.Label(input_frame, text="Voltage Source (V):").grid(row=1, column=0, sticky=tk.W)
    source_entry = ttk.Entry(input_frame, width=10)
    source_entry.grid(row=1, column=1, sticky=tk.W)

    add_button = ttk.Button(input_frame, text="Add Element", command=add_element)
    add_button.grid(row=2, column=0, columnspan=2, sticky=tk.W)

    elements_frame = ttk.Frame(main_frame, padding="5")
    elements_frame.grid(row=0, column=1, sticky=tk.W)

    elements_list = tk.Listbox(elements_frame, height=10, width=30, bg="white")
    elements_list.grid(row=0, column=0, sticky=tk.W)

    circuit_frame = ttk.Frame(main_frame, padding="5", relief="groove")
    circuit_frame.grid(row=1, column=0, columnspan=2, sticky=tk.W)

    result_label = ttk.Label(main_frame, text="", wraplength=400)
    result_label.grid(row=2, column=0, columnspan=2, sticky=tk.W)

    calculate_button = ttk.Button(main_frame, text="Calculate and Draw", command=calculate_and_draw)
    calculate_button.grid(row=3, column=0, columnspan=2, sticky=tk.W)

    resistors = []
    sources = []

    root.mainloop()


if __name__ == "__main__":
    main_window()
