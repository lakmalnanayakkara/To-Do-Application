import { Component, OnInit } from '@angular/core';
import { TaskService } from '../service/task.service';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-list-task',
  standalone: false,
  templateUrl: './list-task.component.html',
  styleUrl: './list-task.component.css',
})
export class ListTaskComponent implements OnInit {
  isLoading = false;
  isError = false;
  errorMessage = '';
  items = undefined;

  public subscriptions: Subscription = new Subscription();

  constructor(private taskService: TaskService, private router: Router) {}

  ngOnInit() {
    this.isLoading = true;
    this.getTaskList();
  }

  closeAlert() {
    this.isError = false;
  }

  getTaskList() {
    const getSub = this.taskService.getTasks().subscribe(
      (data) => {
        this.isLoading = false;
        this.items = data.data;
      },
      (error) => {
        this.isLoading = false;
        this.isError = true;
        this.errorMessage = error.error.data;
      }
    );
    this.subscriptions.add(getSub);
  }

  deleteTask(id: number) {
    this.isLoading = true;
    const deleteSub = this.taskService.deleteTask(id).subscribe(
      (data) => {
        this.isLoading = false;
        this.getTaskList();
        this.router.navigate(['']);
      },
      (error) => {
        this.isLoading = false;
        this.isError = true;
        this.errorMessage = error.error.data;
      }
    );
    this.subscriptions.add(deleteSub);
  }

  updateTask(id: number) {
    this.router.navigate([`/update-task/${id}`]);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
