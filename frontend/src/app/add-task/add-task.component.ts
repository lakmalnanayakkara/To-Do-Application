import { Component, ViewEncapsulation, type OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { TaskDetails, TaskService } from '../service/task.service';
import { ActivatedRoute, Router } from '@angular/router';
import { formatDate } from '@angular/common';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-add-task',
  standalone: false,
  templateUrl: './add-task.component.html',
  styleUrl: './add-task.component.css',
  encapsulation: ViewEncapsulation.None,
})
export class AddTaskComponent implements OnInit {
  readonly startDate = new Date();
  isLoading = false;
  isError = false;
  message = '';
  isSuccess = false;

  taskId: number = undefined;

  public subscriptions: Subscription = new Subscription();

  constructor(
    private taskService: TaskService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  addTaskForm = new FormGroup({
    title: new FormControl('', [Validators.required]),
    taskDate: new FormControl('', [Validators.required]),
    taskTime: new FormControl<Date | null>(null, [Validators.required]),
    description: new FormControl('', [Validators.required]),
  });

  ngOnInit() {
    this.isLoading = true;
    this.taskId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.taskId) {
      const updateSub = this.taskService.getTask(this.taskId).subscribe(
        (data) => {
          this.isLoading = false;
          const backendTime = data.data.taskTime;
          const [hours, minutes] = backendTime.split(':').map(Number);
          const timeDate = new Date();
          timeDate.setHours(hours);
          timeDate.setMinutes(minutes);

          this.addTaskForm.controls.title.setValue(data.data.taskTitle);
          this.addTaskForm.controls.taskDate.setValue(data.data.taskDate);
          this.addTaskForm.controls.taskTime.setValue(timeDate);
          this.addTaskForm.controls.description.setValue(data.data.description);
        },
        (error) => {
          this.isLoading = false;
          this.isError = true;
        }
      );
      this.subscriptions.add(updateSub);
    } else {
      this.isLoading = false;
    }
  }

  onSubmit() {
    this.isError = false;
    this.isSuccess = false;
    this.isLoading = true;

    const data: TaskDetails = {
      taskTitle: this.addTaskForm.controls.title.value,
      taskDate: this.formatDate(
        new Date(this.addTaskForm.controls.taskDate.value)
      ),
      taskTime: this.formatTime(
        new Date(this.addTaskForm.controls.taskTime.value)
      ),
      description: this.addTaskForm.controls.description.value,
    };

    if (this.taskId) {
      const sub = this.taskService.updateTask(this.taskId, data).subscribe(
        (data) => {
          this.isLoading = false;
          this.isSuccess = true;
          this.isError = false;
          this.message = `${data.data.taskTitle} task updated successfully.`;
          this.addTaskForm.reset();
          this.router.navigate(['']);
        },
        (error) => {
          this.isLoading = false;
          this.isSuccess = false;
          this.isError = true;
          this.message = error.error.data;
        }
      );
      this.subscriptions.add(sub);
    } else {
      const sub = this.taskService.addTask(data).subscribe(
        (data) => {
          this.isLoading = false;
          this.isSuccess = true;
          this.isError = false;
          this.message = `${data.data.taskTitle} task added successfully.`;
          this.addTaskForm.reset();
        },
        (error) => {
          this.isLoading = false;
          this.isSuccess = false;
          this.isError = true;
          this.message = error.error.data;
        }
      );
      this.subscriptions.add(sub);
    }
  }

  public formatDate(date: Date) {
    return date.toISOString().split('T')[0];
  }

  public formatTime(time: Date): string {
    return time.toLocaleTimeString('en-US', {
      hour12: false,
      hour: '2-digit',
      minute: '2-digit',
    });
  }

  closeAlert() {
    this.isError = false;
    this.isSuccess = false;
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
